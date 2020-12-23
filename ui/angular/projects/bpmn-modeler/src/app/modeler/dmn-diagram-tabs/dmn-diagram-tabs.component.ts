import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewEncapsulation,
} from "@angular/core";

@Component({
  selector: "dmn-diagram-tabs",
  templateUrl: "./dmn-diagram-tabs.component.html",
  styleUrls: ["./dmn-diagram-tabs.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class DmnDiagramTabsComponent {
  //implements OnInit {
  @Input() views: [];
  @Output() tabClick = new EventEmitter<any>();
  activeTabId = 0;

  CLASS_NAMES = {
    drd: "dmn-icon-lasso-tool",
    decisionTable: "dmn-icon-decision-table",
    literalExpression: "dmn-icon-literal-expression",
  };

  getClassName(aView: any) {
    return this.CLASS_NAMES[aView.type];
  }

  onTabClick(aView: any, tabIndex: number) {
    this.tabClick.emit(aView);
    this.activeTabId = tabIndex;
  }
}
