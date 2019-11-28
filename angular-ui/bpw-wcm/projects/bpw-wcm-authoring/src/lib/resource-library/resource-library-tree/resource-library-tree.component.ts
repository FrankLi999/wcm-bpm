import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { takeUntil } from 'rxjs/operators';
import { select, Store } from '@ngrx/store';
import { MatDialog } from '@angular/material/dialog';

import { fuseAnimations } from 'bpw-components';
import { WcmOperation, JsonForm, ModeshapeService, WcmService } from 'bpw-wcm-service';
import * as fromStore from 'bpw-wcm-service';
import { WcmNavigatorComponent } from '../../components/wcm-navigator/wcm-navigator.component';

@Component({
  selector: 'resource-library-tree',
  templateUrl: './resource-library-tree.component.html',
  styleUrls: ['./resource-library-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class ResourceLibraryTreeComponent extends WcmNavigatorComponent implements OnInit, OnDestroy {
  
  functionMap: {[key:string]:Function}= {};
  jsonFormMap: {[key:string]: JsonForm} = {}; //TODO: it is loaded asynchronously during ngInit
  constructor(
    protected wcmService: WcmService,
    private modeshapeService: ModeshapeService,
    protected store: Store<fromStore.WcmAppState>,
    protected matDialog: MatDialog
  ) {
    super(wcmService, store, matDialog);
  }

  ngOnInit() {
    this.rootNode = '';
    this.rootNodeType = 'bpw:library';
    this.functionMap['Create.library'] = this.createLibrary;
    this.nodeFileter = {
      nodePath: '',
      nodeTypes: ['bpw:library']
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

  createLibrary() {
    console.log('create library');
  }
}