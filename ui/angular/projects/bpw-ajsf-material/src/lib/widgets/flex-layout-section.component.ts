import { AbstractControl } from "@angular/forms";
import { Component, Input, OnInit } from "@angular/core";
import { JsonSchemaFormService } from "@bpw/ajsf-core";

@Component({
  // tslint:disable-next-line:component-selector
  selector: "flex-layout-section-widget",
  templateUrl: "./flex-layout-section.component.html",
  styleUrls: ["./flex-layout-section.component.scss"],
})
export class FlexLayoutSectionComponent implements OnInit {
  formControl: AbstractControl;
  controlName: string;
  controlValue: any;
  controlDisabled = false;
  boundControl = false;
  options: any;
  expanded = true;
  containerType = "div";
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];

  constructor(private jsf: JsonSchemaFormService) {}

  get sectionTitle() {
    return this.options.notitle ? null : this.jsf.setItemTitle(this);
  }

  ngOnInit() {
    this.jsf.initializeControl(this);
    this.options = this.layoutNode.options || {};
    this.expanded =
      typeof this.options.expanded === "boolean"
        ? this.options.expanded
        : !this.options.expandable;
    switch (this.layoutNode.type) {
      case "section":
      case "array":
      case "fieldset":
      case "advancedfieldset":
      case "authfieldset":
      case "optionfieldset":
      case "selectfieldset":
        this.containerType = "fieldset";
        break;
      case "card":
        this.containerType = "card";
        break;
      case "expansion-panel":
        this.containerType = "expansion-panel";
        break;
      default:
        // 'div', 'flex', 'tab', 'conditional', 'actions'
        this.containerType = "div";
    }
  }

  toggleExpanded() {
    if (this.options.expandable) {
      this.expanded = !this.expanded;
    }
  }

  // Set attributes for flexbox container
  // (child attributes are set in flex-layout-root.component)
  getFlexAttribute(attribute: string) {
    const flexActive: boolean =
      this.layoutNode.type === "flex" ||
      !!this.options.displayFlex ||
      this.options.display === "flex";
    // if (attribute !== 'flex' && !flexActive) { return null; }
    switch (attribute) {
      case "is-flex":
        return flexActive;
      case "display":
        return flexActive ? "flex" : "initial";
      case "flex-direction":
      case "flex-wrap":
        const index = ["flex-direction", "flex-wrap"].indexOf(attribute);
        return (
          (this.options["flex-flow"] || "").split(/\s+/)[index] ||
          this.options[attribute] ||
          ["column", "nowrap"][index]
        );
      case "justify-content":
      case "align-items":
      case "align-content":
        return this.options[attribute];
      case "layout":
        return (this.options.fxLayout || "row") + this.options.fxLayoutWrap
          ? " " + this.options.fxLayoutWrap
          : "";
    }
  }

  get disabled(): boolean {
    return this.options?.readonly || this.jsf.formOptions.readonly;
  }
}
