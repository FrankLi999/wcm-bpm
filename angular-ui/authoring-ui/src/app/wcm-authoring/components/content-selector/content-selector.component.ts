import { Component, OnInit, OnDestroy, Input} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { WcmNavigatorComponent } from '../wcm-navigator/wcm-navigator.component';
import * as fromStore from '../../store';
import { WcmService } from '../../service/wcm.service';
import { WcmOperation } from '../../model';

@Component({
  selector: 'content-selector',
  templateUrl: './content-selector.component.html',
  styleUrls: ['./content-selector.component.scss']
})
export class ContentSelectorComponent extends WcmNavigatorComponent implements OnInit, OnDestroy {
  functionMap: {[key:string]: Function};
  @Input() selectedContentItems: string[];
  @Input() authoringTemplate: string;

  constructor(
    protected wcmService: WcmService,
    protected store: Store<fromStore.WcmAppState>,
    protected matDialog: MatDialog
  ) { 
    super(wcmService, store, matDialog);
  }

  ngOnInit() {
    this.rootNode = 'rootSiteArea';
    this.rootNodeType = 'bpw:siteArea';
    this.nodeFileter = {
      nodePath: '',
      nodeTypes: ['bpw:siteArea', 'bpw:content'],
      filters: {
        'bpw:content' : {
          'bpw:authoringTemplate': this.authoringTemplate
        }
      }
    }

    this.functionMap = {
      'Select.content': this.selectContent
    };
  
  
    this.operationMap	= {
      "bpw:content": [{
          jcrType: 'bpw:content',
          resourceName: 'content',
          operation: 'Select',
          defaultTitle: 'Select Content Item',
          defaultIcon: 'create'
        }
      ]
    }
    super.ngOnInit();
  }

  doNodeOperation(item: string, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(this);
  }

  disableOperation(item: string, operation: WcmOperation) {
    if ('bpw:content' === operation.jcrType) {
      return this.selectedContentItems.includes(this.activeNode.wcmPath);
    } else {
      return false;
    }
  }

  selectContent() {
    return this.selectedContentItems.push(this.activeNode.wcmPath);
  }

  removeSelection(index: number) {
    return this.selectedContentItems.splice(index, 1);
  }
}