import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { WcmService } from 'bpw-wcm-service';
import { BaseMewResourceDialog } from '../base-new-resource-dialog';
import { SiteArea } from 'bpw-wcm-service';
@Component({
  selector: 'app-new-sitearea-dialog',
  templateUrl: './new-sitearea-dialog.component.html',
  styleUrls: ['./new-sitearea-dialog.component.scss']
})
export class NewSiteareaDialogComponent extends BaseMewResourceDialog implements OnInit {
  constructor(
    public matDialogRef: MatDialogRef<NewSiteareaDialogComponent>,
    private wcmService: WcmService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { 
    super(data);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Public methods
  // -----------------------------------------------------------------------------------------------------

  createSitearea(formData: any) {
    const sa: SiteArea = {
      ...formData,
      repository: this.data.repositoryName,
      nodePath: this.data.nodePath,
      workspace: this.data.workspaceName,
    }
    this.wcmService.createSiteArea(sa)
      .subscribe((event: any) => {
        console.log(event)
      });    
  }
}
