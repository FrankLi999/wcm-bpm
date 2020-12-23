import { Component, OnInit, Inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { Store } from "@ngrx/store";

import { Library, WcmConstants } from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { BaseMewResourceDialog } from "../base-new-resource-dialog";
@Component({
  selector: "bpw-wcm-authoring-new-library-dialog",
  templateUrl: "./new-library-dialog.component.html",
  styleUrls: ["./new-library-dialog.component.scss"],
})
export class NewLibraryDialogComponent extends BaseMewResourceDialog
  implements OnInit {
  constructor(
    public matDialogRef: MatDialogRef<NewLibraryDialogComponent>,
    protected store: Store<fromStore.WcmAppState>,
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

  createLibrary(formData: any) {
    this.store.dispatch(
      new fromStore.CreateLibrary(<Library>{
        repository: WcmConstants.REPO_BPWIZARD,
        workspace: WcmConstants.WS_DEFAULT,
        name: formData.properties.name,
        title: formData.properties.title,
        description: formData.properties.description,
        language: formData.elements.name,
      })
    );
  }
}
