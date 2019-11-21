import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ModeshapeService } from 'bpw-wcm-service';
import { BaseMewResourceDialog } from '../base-new-resource-dialog';
@Component({
  selector: 'app-new-folder-dialog',
  templateUrl: './new-folder-dialog.component.html',
  styleUrls: ['./new-folder-dialog.component.scss']
})
export class NewFolderDialogComponent extends BaseMewResourceDialog implements OnInit {
  constructor(
    public matDialogRef: MatDialogRef<NewFolderDialogComponent>,
    private modeshapeService: ModeshapeService,
    @Inject(MAT_DIALOG_DATA) data: any
  ) { 
    super(data);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Public methods
  // -----------------------------------------------------------------------------------------------------

  createFolder(formData: any) {
    let newFolderBody = {
      "jcr:primaryType": "nt:folder"
    }
    this.modeshapeService.postItems(this.data.repositoryName, this.data.workspaceName, `${this.data.nodePath}/${formData.name}`, newFolderBody)
      .subscribe((event: any) => {
      });    
  }
}
