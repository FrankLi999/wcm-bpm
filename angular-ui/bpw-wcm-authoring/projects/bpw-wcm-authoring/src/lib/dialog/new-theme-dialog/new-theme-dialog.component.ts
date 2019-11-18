import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ModeshapeService } from 'bpw-wcm-service';
import { BaseMewResourceDialog } from '../base-new-resource-dialog';
@Component({
  selector: 'app-new-theme-dialog',
  templateUrl: './new-theme-dialog.component.html',
  styleUrls: ['./new-theme-dialog.component.scss']
})
export class NewThemeDialogComponent extends BaseMewResourceDialog implements OnInit {
  constructor(
    public matDialogRef: MatDialogRef<NewThemeDialogComponent>,
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

    createTheme(formData: any) {
      let newThemeBody = {
        "jcr:primaryType":"bpw:themeType",
        "bpw:themeName": formData.name,
        "bpw:title": formData.title
      };
      this.modeshapeService.postItems(this.data.repositoryName, this.data.workspaceName, `${this.data.nodePath}/${formData.name}`, newThemeBody)
        .subscribe((event: any) => {
          // this.newThemeForm.reset();
        });    
    }
}
