import { Component, OnInit, Inject } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ModeshapeService } from '../../service/modeshape.service';

@Component({
  selector: 'app-new-theme-dialog',
  templateUrl: './new-theme-dialog.component.html',
  styleUrls: ['./new-theme-dialog.component.scss']
})
export class NewThemeDialogComponent implements OnInit {
  newThemeForm: FormGroup;
  constructor(
    public matDialogRef: MatDialogRef<NewThemeDialogComponent>,
    private modeshapeService: ModeshapeService,
    @Inject(MAT_DIALOG_DATA) private data: any
  ) { 
    this.newThemeForm = this.createNewThemeForm();
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
    createNewThemeForm(): FormGroup {
        return new FormGroup({
          themeName : new FormControl(''),
          title : new FormControl('')
        });
    }

    createTheme() {
      let newThemeBody = {
        "jcr:primaryType":"bpw:themeType",
        "bpw:themeName": this.newThemeForm.get('themeName').value,
        "bpw:title": this.newThemeForm.get('title').value
      };
      this.modeshapeService.postItems(this.data.repositoryName, this.data.workspaceName, `${this.data.nodePath}/${this.newThemeForm.get('themeName').value}`, newThemeBody)
        .subscribe((event: any) => {
          this.newThemeForm.reset();
        });    
    }
}
