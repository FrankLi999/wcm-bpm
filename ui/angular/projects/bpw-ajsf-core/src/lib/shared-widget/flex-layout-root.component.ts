import { ChangeDetectionStrategy, Component, Input } from "@angular/core";
import { JsonSchemaFormService } from "../json-schema-form.service";

@Component({
  // tslint:disable-next-line:component-selector
  selector: "flex-layout-root-widget",
  templateUrl: "./flex-layout-root.component.html",
  changeDetection: ChangeDetectionStrategy.Default,
})
export class FlexLayoutRootComponent {
  @Input() dataIndex: number[];
  @Input() layoutIndex: number[];
  @Input() layout: any[];
  @Input() isFlexItem = false;

  constructor(private jsf: JsonSchemaFormService) {}

  removeItem(item) {
    this.jsf.removeItem(item);
  }

  // Set attributes for flexbox child
  // (container attributes are set in flex-layout-section.component)
  getFlexAttribute(node: any, attribute: string) {
    const index = ["flex-grow", "flex-shrink", "flex-basis"].indexOf(attribute);
    return (
      ((node.options || {}).flex || "").split(/\s+/)[index] ||
      (node.options || {})[attribute] ||
      ["1", "1", "auto"][index]
    );
  }

  showWidget(layoutNode: any): boolean {
    return this.jsf.evaluateCondition(layoutNode, this.dataIndex);
  }
}
