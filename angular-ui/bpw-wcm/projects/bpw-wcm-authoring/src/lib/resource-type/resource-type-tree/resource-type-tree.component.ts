import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { Router } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { select, Store } from '@ngrx/store';
import { MatDialog } from '@angular/material/dialog';

import { fuseAnimations } from 'bpw-components';
import { WcmOperation, JsonForm } from 'bpw-wcm-service';
import * as fromStore from 'bpw-wcm-service';
import { ModeshapeService } from 'bpw-wcm-service';
import { WcmService } from 'bpw-wcm-service';
import { WcmNavigatorComponent } from '../../components/wcm-navigator/wcm-navigator.component';

@Component({
  selector: 'resource-type-tree',
  templateUrl: './resource-type-tree.component.html',
  styleUrls: ['./resource-type-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class ResourceTypeTreeComponent extends WcmNavigatorComponent implements OnInit, OnDestroy {

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
    this.rootNode = 'authoringTemplate';
    this.rootNodeType = 'bpw:authoringTemplateFolder';
    this.functionMap['Create.authoringTemplate'] = this.createAuthoringTemplate;
    this.functionMap['Edit.authoringTemplate'] = this.editAuthoringTemplate;
    this.functionMap['Delete.authoringTemplate'] = this.removeAuthoringTemplate;

    this.nodeFileter = {
      nodePath: '',
      nodeTypes: ['bpw:authoringTemplate', 'bpw:folder']
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

  createAuthoringTemplate() {
    const node = this.activeNode;
    const library = node.wcmPath.split('/', 4)[3];
    this.router.navigate(['/wcm-authoring/resource-type/new'],{
      queryParams: {
        library: library,
        repository: node.repository,
        workspace: node.workspace,
        wcmPath: node.wcmPath,
        editing: false
      } 
    });
  }

  removeAuthoringTemplate() {
    console.log('removeContentAreaLayout');
  }

  editAuthoringTemplate() {
    const node = this.activeNode;
    const library = node.wcmPath.split('/', 4)[3];
    this.router.navigate(['/wcm-authoring/resource-type/edit'],{
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