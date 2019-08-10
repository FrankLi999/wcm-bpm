import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { WcmService } from '../../service/wcm.service';
import { BaseMewResourceDialog } from '../base-new-resource-dialog';

@Component({
  selector: 'app-new-page-dialog',
  templateUrl: './new-page-dialog.component.html',
  styleUrls: ['./new-page-dialog.component.scss']
})
export class NewPageDialogComponent extends BaseMewResourceDialog implements OnInit {
  constructor(
    public matDialogRef: MatDialogRef<NewPageDialogComponent>,
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

  createPage(formData: any) {
    const page = {
      ...formData,
      repository: this.data.repositoryName,
      nodePath: this.data.nodePath,
      workspace: this.data.workspaceName
    }
    this.wcmService.createPage(page).subscribe((event: any) => {
        console.log(event)
    });  
  }
}
