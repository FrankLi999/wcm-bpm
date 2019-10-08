import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { WcmNavigatorComponent } from '../../components/wcm-navigator/wcm-navigator.component';
import * as fromStore from '../../store';
import { WcmService } from '../../service/wcm.service';
import { WcmOperation } from '../../model';
import { WcmItemFlatTreeNode } from '../../components/wcm-navigator/wcm-navigator.component';

@Component({
  selector: 'authoring-template-selector',
  templateUrl: './authoring-template-selector.component.html',
  styleUrls: ['./authoring-template-selector.component.scss']
})
export class AuthoringTemplateSelectorComponent extends WcmNavigatorComponent implements OnInit, OnDestroy {
  functionMap: {[key:string]: Function};

  @Output() authoringTemplateSelected: EventEmitter<any> = new EventEmitter();
  constructor(
    protected wcmService: WcmService,
    protected store: Store<fromStore.WcmAppState>,
    protected matDialog: MatDialog
  ) { 
    super(wcmService, store, matDialog);
  }

  ngOnInit() {
    this.rootNode = 'authoringTemplate';
    this.rootNodeType = 'bpw:authoringTemplateFolder';
    this.nodeFileter = {
      nodePath: '',
      nodeTypes: ['bpw:authoringTemplate']
    }

    this.operationMap	= {};
    super.ngOnInit();
  }

  loadChildren(node: WcmItemFlatTreeNode, onlyFirstTime = true) {
    super.loadChildren(node, onlyFirstTime);
    if (node.nodeType === 'bpw:authoringTemplate') {
      this.authoringTemplateSelected.emit(node);
    }
  }

  doNodeOperation(item: string, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(this);
  }

}