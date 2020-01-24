import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { select, Store } from '@ngrx/store';
import { MatDialog } from '@angular/material/dialog';
import { wcmAnimations } from 'bpw-common';
import { WcmOperation, ModeshapeService, WcmService } from 'bpw-wcm-service';
import * as fromStore from 'bpw-wcm-service';
import { WcmNavigatorComponent } from '../../components/wcm-navigator/wcm-navigator.component';

@Component({
  
  selector: 'render-template-tree',
  templateUrl: './render-template-tree.component.html',
  styleUrls: ['./render-template-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : wcmAnimations
})
export class RenderTemplateTreeComponent extends WcmNavigatorComponent implements OnInit, OnDestroy {

  functionMap: {[key:string]:Function}= {};
  //jsonFormMap: {[key:string]: JsonForm} = {}; //TODO: it is loaded asynchronously during ngInit
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
    this.rootNode = 'renderTemplate';
    this.rootNodeType = 'bpw:renderTemplateFolder';
    this.functionMap['Create.renderTemplate'] = this.createRenderTemplate;
    this.functionMap['Edit.renderTemplate'] = this.editRenderTemplate;
    this.functionMap['Delete.renderTemplate'] = this.removeRenderTemplate;

    this.nodeFileter = {
      nodePath: '',
      nodeTypes: ['bpw:renderTemplate', 'bpw:folder']
    }
    
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

  createRenderTemplate() {
    const node = this.activeNode;
    const library = node.wcmPath.split('/', 4)[3];
    this.router.navigate(['/wcm-authoring/render-template/new'],{
      queryParams: {
        library: library,
        repository: node.repository,
        workspace: node.workspace,
        wcmPath: node.wcmPath,
        editing: false
      } 
    });
  }

  removeRenderTemplate() {
    console.log('removeContentAreaLayout');
  }

  editRenderTemplate() {
    const node = this.activeNode;
    const library = node.wcmPath.split('/', 4)[3];
    this.router.navigate(['/wcm-authoring/render-template/edit'],{
      queryParams: {
        library: library,
        repository: node.repository,
        workspace: node.workspace,
        wcmPath: node.wcmPath,
        editing: true
      } 
    });
  }
}