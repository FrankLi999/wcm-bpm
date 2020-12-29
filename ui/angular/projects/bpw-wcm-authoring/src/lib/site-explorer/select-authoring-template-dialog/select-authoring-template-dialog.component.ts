import { Component, OnInit } from "@angular/core";
import { MatDialogRef } from "@angular/material/dialog";
import { WcmItemFlatTreeNode } from "../../components/wcm-tree/model/wcm-tree.model";

@Component({
  selector: "select-authoring-template-dialog.component",
  templateUrl: "./select-authoring-template-dialog.component.html",
  styleUrls: ["./select-authoring-template-dialog.component.scss"],
})
export class SelectAuthoringTemplateDialogComponent implements OnInit {
  selectedAuthoringTemplate: string;
  constructor(
    public dialogRef: MatDialogRef<SelectAuthoringTemplateDialogComponent>
  ) {}

  ngOnInit() {
    this.selectedAuthoringTemplate = "";
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onOKClick() {
    this.dialogRef.close({
      selectedAuthoringTemplate: this.selectedAuthoringTemplate,
    });
  }

  authoringTemplateSelected(node: WcmItemFlatTreeNode) {
    this.selectedAuthoringTemplate = node.data.wcmPath;
  }
}
