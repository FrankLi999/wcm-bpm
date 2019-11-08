import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { select, Store } from '@ngrx/store';
import { MatDialog } from '@angular/material/dialog';
import { WcmOperation, JsonForm } from '../../model';
import * as fromStore from '../../store';
import { ModeshapeService } from '../../service/modeshape.service';
import { WcmService } from '../../service/wcm.service';
import { WcmNavigatorComponent } from '../../components/wcm-navigator/wcm-navigator.component';

import { UploadZipfileDialogComponent } from '../../dialog/upload-zipfile-dialog/upload-zipfile-dialog.component';
import { NewFolderDialogComponent } from '../../dialog/new-folder-dialog/new-folder-dialog.component';
import { NewThemeDialogComponent } from '../../dialog/new-theme-dialog/new-theme-dialog.component';
import { SelectAuthoringTemplateDialogComponent } from '../select-authoring-template-dialog/select-authoring-template-dialog.component';

@Component({
  selector: 'site-explorer',
  templateUrl: './site-explorer.component.html',
  styleUrls: ['./site-explorer.component.scss']
})
export class SiteExplorerComponent extends WcmNavigatorComponent implements OnInit, OnDestroy {
  functionMap: {[key:string]:Function}= {};
  jsonFormMap: {[key:string]: JsonForm} = {}; //TODO: it is loaded asynchronously during ngInit
  constructor(
    protected wcmService: WcmService,
    private modeshapeService: ModeshapeService,
    protected store: Store<fromStore.WcmAppState>,
    protected matDialog: MatDialog,
    private router: Router
  ) {
    super(wcmService, store, matDialog);
  }

  ngOnInit() {
    this.rootNode = 'rootSiteArea';
    this.rootNodeType = 'bpw:siteArea';
    this.functionMap['Upload.file'] = this.uploadZipFile;
    this.functionMap['Create.folder'] = this.createFolder;
    this.functionMap['Create.theme'] = this.createTheme;
    this.functionMap['Remove.folder'] = this.removeItem;
    this.functionMap['Remove.file'] = this.removeItem;
    this.functionMap['Delete.theme'] = this.removeItem;
    
    this.functionMap['Create.siteArea'] = this.createSiteArea;
    this.functionMap['Preview.siteArea'] = this.previewSiteArea;
    this.functionMap['Edit.siteArea'] = this.editSiteArea;
    this.functionMap['Delete.siteArea'] = this.removeItem;
    this.functionMap['Create.content'] = this.createContent;
    this.functionMap['Edit.content'] = this.editContent;
    this.functionMap['Delete.content'] = this.removeItem;

    this.nodeFileter = {
      nodePath: '',
      nodeTypes: ['bpw:siteArea', 'bpw:content', 'bpw:folder', 'bpw:file']
    }
    this.store.pipe(
      takeUntil(this.unsubscribeAll),
      select(fromStore.getJsonForms)
    ).subscribe(
      (jsonForms: {[key:string]:JsonForm}) => {
        if (jsonForms) {
          this.jsonFormMap = jsonForms;
        }
      },
      response => {
        console.log("getAuthoringTemplateAsJsonSchema call ended in error", response);
        console.log(response);
      },
      () => {
        console.log("getAuthoringTemplateAsJsonSchema observable is now completed.");
      }
    );
    this.store.pipe(
      takeUntil(this.unsubscribeAll),
      select(fromStore.getOperations)).subscribe(
      (operations: {[key: string]: WcmOperation[]}) => {
        if (operations) {
          this.operationMap = operations;
        }
      },
      response => {
        console.log("GET call in error", response);
        console.log(response);
      },
      () => {
        console.log("The GET observable is now completed.");
      }
    );
    super.ngOnInit();
  }

  /**
    * On destroy
    */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.loadError && this.store.dispatch(new fromStore.WcmSystemClearError());
  }

  doNodeOperation(item: String, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(this);
  }

  uploadZipFile() {
    const node =  this.activeNode;
    let dialogRef = this.matDialog.open(UploadZipfileDialogComponent, {
      panelClass: 'zipfile-upload-dialog',
      data: { 
        nodePath: node.wcmPath,
        repositoryName: node.repository,
        workspaceName: node.workspace
      }
    });
    dialogRef.afterClosed()
      .subscribe(response => {
        this.load(node);
    });
  }

  createFolder() {
    const node =  this.activeNode;
    let dialogRef = this.matDialog.open(NewFolderDialogComponent, {
      panelClass: 'folder-new-dialog',
      data: {
        jsonFormObject: this.jsonFormMap['bpwizard/default/system/folderType'].formSchema,
        nodePath: node.wcmPath,
        repositoryName: node.repository,
        workspaceName: node.workspace
      }
    });
    dialogRef.afterClosed()
      .subscribe(response => {
        this.load(node);
          
    });
  }

  createTheme() {
    const node =  this.activeNode;
    // node: JcrFlatNode, matDialog: MatDialog, modeshapeService: ModeshapeService, 
    //   jcrNodeMap: Map<string, JcrNode>, nodeMap: Map<string, JcrFlatNode>, dataChange: BehaviorSubject<JcrNode[]>, callback: Function) {
    let dialogRef = this.matDialog.open(NewThemeDialogComponent, {
      panelClass: 'theme-new-dialog',
      data: { 
        jsonFormObject: this.jsonFormMap['bpwizard/default/system/themeType'].formSchema,
        nodePath: node.wcmPath,
        repositoryName: node.repository,
        workspaceName: node.workspace
      }
    });
    dialogRef.afterClosed()
      .subscribe(response => {
        this.load(node);          
    });
  }

  removeItem() {
    const node =  this.activeNode;
    // node: JcrFlatNode, matDialog: MatDialog, modeshapeService: ModeshapeService, 
    //   jcrNodeMap: Map<string, JcrNode>, nodeMap: Map<string, JcrFlatNode>, dataChange: BehaviorSubject<JcrNode[]>, callback: Function) {
    this.modeshapeService.deleteItem(
      node.repository,
      node.workspace,
      node.wcmPath      
    ).subscribe(
      (event: any) => {
        if (event.type===4) {
          this.nodeRemoved(node);
        }
      },
      response => {
        console.log("removeFolder call in error", response);
        console.log(response);
      },
      () => {
        console.log("removeFolder observable is now completed.");
      });
  }

  previewSiteArea() {
    const node =  this.activeNode;
    console.log('preview site area:', node);
    this.router.navigate(['/wcm-authoring/preview'],{
      queryParams: {
        siteArea: node.wcmPath
      } 
    });
  }

  createSiteArea() {
    const node =  this.activeNode;

    this.router.navigate(
      ['/wcm-authoring/site-explorer/new-sa'], 
      { queryParams: {
          nodePath: node.wcmPath,
          repository: node.repository,
          workspace: node.workspace,
          editing: false
        } 
      }            
    );
  }
  
  editSiteArea() {
    const node =  this.activeNode;
    this.router.navigate(
      ['/wcm-authoring/site-explorer/edit-sa'], 
      { queryParams: {
          nodePath: node.wcmPath,
          repository: node.repository,
          workspace: node.workspace,
          editing: true
        } 
      }            
    );
  }

  createContent() {
    const node =  this.activeNode;
    let dialogRef = this.matDialog.open(SelectAuthoringTemplateDialogComponent, {
      panelClass: 'content-new-dialog',
    });
    dialogRef.afterClosed()
      .subscribe(response => {
        console.log(response);
        if (response && response.selectedAuthoringTemplate) {
          this.router.navigate(
            ['/wcm-authoring/site-explorer/new-content'], 
            { queryParams: {
                authoringTemplate: response.selectedAuthoringTemplate,
                nodePath: node.wcmPath,
                repository: node.repository,
                workspace: node.workspace,
                editing: false
              } 
            }            
          );
        }
        //this.load(node);                
    });
  }

  editContent() {
    const node =  this.activeNode;
    this.wcmService.getContentItem(node.repository, node.workspace, node.wcmPath).subscribe(
      response => { 
        console.log('editCurrentNode', response)
      }
    )

    this.router.navigate(
      ['/wcm-authoring/site-explorer/edit-content'], 
      { queryParams: {
          nodePath: node.wcmPath,
          repository: node.repository,
          workspace: node.workspace,
          editing: true
        } 
      }            
    );
  }
}