import { Component, OnInit, Inject } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ModeshapeService } from '../../service/modeshape.service';

@Component({
  selector: 'app-new-folder-dialog',
  templateUrl: './new-folder-dialog.component.html',
  styleUrls: ['./new-folder-dialog.component.scss']
})
export class NewFolderDialogComponent implements OnInit {
  newFolderForm: FormGroup;
  constructor(
    public matDialogRef: MatDialogRef<NewFolderDialogComponent>,
    private modeshapeService: ModeshapeService,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) { 
    this.newFolderForm = this.createNewFolderForm();
  }

  ngOnInit() {
  }

  // -----------------------------------------------------------------------------------------------------
    // @ Public methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Create compose form
     *
     * @returns {FormGroup}
     */
    createNewFolderForm(): FormGroup {
        return new FormGroup({
          folderName : new FormControl('')
        });
    }

    createFolder() {
      let newFolderBody = {
        "jcr:primaryType": "nt:folder"
      }
      this.modeshapeService.postItems(this.data.repositoryName, this.data.workspaceName, `${this.data.nodePath}/${this.newFolderForm.get('folderName').value}`, newFolderBody)
        .subscribe((event: any) => {
          this.newFolderForm.reset();
        });    
    }
}
