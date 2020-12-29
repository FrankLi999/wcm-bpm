import {
  Component,
  Input,
  OnInit,
  ViewChild,
  ViewEncapsulation,
} from "@angular/core";
import { JsonSchemaFormService } from "@bpw/ajsf-core";
import { MatStepper } from "@angular/material/stepper";

@Component({
  // tslint:disable-next-line:component-selector
  selector: "material-stepper-widget",
  templateUrl: "./material-stepper.component.html",
  styleUrls: ["./material-stepper.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class MaterialStepperComponent implements OnInit {
  options: any;
  @Input() layoutNode: any;
  @Input() layoutIndex: number[];
  @Input() dataIndex: number[];
  @ViewChild("stepper", { static: true }) private stepper: MatStepper;

  constructor(private jsf: JsonSchemaFormService) {}

  ngOnInit() {
    this.options = this.layoutNode.options || {};
  }

  hasNextStep() {
    return (
      this.stepper.selectedIndex !==
      (this.stepper.steps
        ? this.stepper.steps.length - 1
        : this.layoutNode.items.length - 1)
    );
  }

  setTabTitle(item: any, index: number): string {
    return this.jsf.setArrayItemTitle(this, item, index);
  }
}
