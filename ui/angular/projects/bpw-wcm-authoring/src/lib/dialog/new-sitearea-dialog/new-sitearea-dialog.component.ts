import { Component, OnInit, Inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

import { SiteArea, SiteAreaService } from "bpw-wcm-service";
import { BaseMewResourceDialog } from "../base-new-resource-dialog";

@Component({
  selector: "app-new-sitearea-dialog",
  templateUrl: "./new-sitearea-dialog.component.html",
  styleUrls: ["./new-sitearea-dialog.component.scss"]
})
export class NewSiteareaDialogComponent extends BaseMewResourceDialog
  implements OnInit {
  constructor(
    public matDialogRef: MatDialogRef<NewSiteareaDialogComponent>,
    private siteAreaService: SiteAreaService,
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
      wcmPath: this.data.wcmPath,
      workspace: this.data.workspaceName
    };
    this.siteAreaService.createSiteArea(sa).subscribe((event: any) => {
      console.log(event);
    });
  }
}
