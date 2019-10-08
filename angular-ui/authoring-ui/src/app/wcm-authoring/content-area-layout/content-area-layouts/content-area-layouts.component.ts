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

@Component({
  selector: 'app-page-layouts',
  templateUrl: './content-area-layouts.component.html',
  styleUrls: ['./content-area-layouts.component.scss']
})
export class ContentAreaLayoutsComponent extends WcmNavigatorComponent implements OnInit, OnDestroy {

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
    this.rootNode = 'contentAreaLayout';
    this.rootNodeType = 'bpw:contentAreaLayoutFolder';
    this.functionMap['Create.contentAreaLayout'] = this.createContentAreaLayout;
    this.functionMap['Edit.contentAreaLayout'] = this.editContentAreaLayout;
    this.functionMap['Delete.contentAreaLayout'] = this.removeContentAreaLayout;

    this.nodeFileter = {
      nodePath: '',
      nodeTypes: ['bpw:contentAreaLayout', 'bpw:folder']
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
        console.log("getJsonForm call ended in error", response);
        console.log(response);
      },
      () => {
        console.log("getJsonForm observable is now completed.");
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

  createContentAreaLayout() {
    const node = this.activeNode;
    const library = node.wcmPath.split('/', 4)[3];
    this.router.navigate(['/wcm-authoring/content-area-layout/new'],{
      queryParams: {
        library: library,
        repository: node.repository,
        workspace: node.workspace,
        wcmPath: node.wcmPath,
        editing: false
      } 
    });
  }

  removeContentAreaLayout() {
    console.log('removeContentAreaLayout');
  }

  editContentAreaLayout() {
    const node = this.activeNode;
    const library = node.wcmPath.split('/', 4)[3];
    this.router.navigate(['/wcm-authoring/content-area-layout/edit'],{
      queryParams: {
        library: library,
        repository: node.repository,
        workspace: node.workspace,
        layoutName: node.name,
        editing: true
      } 
    });
  }
}