import { Component, OnInit, OnDestroy, Input} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { SiteNavigatorComponent } from '../../components/site-navigator/site-navigator.component';
import * as fromStore from '../../store';
import { WcmService } from '../../service/wcm.service';
import { WcmOperation } from '../../model';

@Component({
  selector: 'content-selector',
  templateUrl: './content-selector.component.html',
  styleUrls: ['./content-selector.component.scss']
})
export class ContentSelectorComponent extends SiteNavigatorComponent implements OnInit, OnDestroy {
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
    this.nodeFileter = {
      nodePath: '',
      nodeTypes: ['bpw:siteArea', 'bpw:content'],
      filters: {
        'bpw:content' : {
          'authoringTemplate': [this.authoringTemplate]
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

  doNodeOperation(item:String, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(this);
  }

  disableOperation(item:String, operation: WcmOperation) {
    return this.selectedContentItems.includes(this.activeNode.wcmPath);
  }

  selectContent(siteNavigator: SiteNavigatorComponent) {
    return this.selectedContentItems.push(siteNavigator.activeNode.wcmPath);
  }

  removeSelection(index: number) {
    return this.selectedContentItems.splice(index, 1);
  }
}