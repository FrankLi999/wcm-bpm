import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { WcmService } from 'bpw-wcm-service';
import { BaseMewResourceDialog } from '../base-new-resource-dialog';
import { SiteConfig } from 'bpw-wcm-service';
@Component({
  selector: 'app-new-site-config-dialog',
  templateUrl: './new-site-config-dialog.component.html',
  styleUrls: ['./new-site-config-dialog.component.scss']
})
export class NewSiteConfigDialogComponent extends BaseMewResourceDialog implements OnInit {
  constructor(
    public matDialogRef: MatDialogRef<NewSiteConfigDialogComponent>,
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

  createSiteConfig(formData: any) {
    const siteConfig: SiteConfig = {
      ...formData,
      repository: this.data.repositoryName,
      workspace: this.data.workspaceName,
      library: this.data.library
    }
    this.wcmService.createSiteConfig(siteConfig)
      .subscribe((event: any) => {
        console.log(event)
      });    
  }
}
