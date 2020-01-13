import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { WcmService } from 'bpw-wcm-service';
import { BaseMewResourceDialog } from '../base-new-resource-dialog';
@Component({
  selector: 'app-new-content-dialog',
  templateUrl: './new-content-dialog.component.html',
  styleUrls: ['./new-content-dialog.component.scss']
})
export class NewContentDialogComponent extends BaseMewResourceDialog implements OnInit {
  constructor(
    public matDialogRef: MatDialogRef<NewContentDialogComponent>,
    private wcmService: WcmService,
    @Inject(MAT_DIALOG_DATA) protected data: any
  ) { 
    super(data);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Public methods
  // -----------------------------------------------------------------------------------------------------

  createContent(formData: any) {
    console.log(formData)
    const contentItem = {
      id: '',
      elements: {...formData.elements},
      properties: {...formData.properties},
      repository: this.data.repositoryName,
      nodePath: this.data.nodePath,
      workspace: this.data.workspaceName,
      lifeCycleStage: 'darft',
      checkedOut: false,
      locked: false,
      authoringTemplate: 'bpwizard/default/design/MyContent',
      workflow: 'bpmn:wcm_content_flow'
    }
    this.wcmService.createAndPublishContentItem(contentItem).subscribe((event: any) => {
        console.log(event)
    });   
  }
}
