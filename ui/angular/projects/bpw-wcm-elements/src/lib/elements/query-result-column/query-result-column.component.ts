import { Component, OnInit, Input, ViewChild, ElementRef } from "@angular/core";

import { ColumnValue, RendererService } from "bpw-wcm-service";
@Component({
  // selector: "query-result-column",
  templateUrl: "./query-result-column.component.html",
  styleUrls: ["./query-result-column.component.scss"],
})
export class QueryResultColumnComponent implements OnInit {
  @Input() column: string;
  @Input() rowindex: number;
  @Input() queryresultid: string;

  rowValue: { [key: string]: ColumnValue };
  @ViewChild("queryColumn", { static: true }) private el: ElementRef;
  constructor(private rendererService: RendererService) {}

  ngOnInit() {
    this.rowValue = this.rowindex
      ? this.rendererService.getQueryResult(this.queryresultid, this.rowindex)
      : null;
    this.el.nativeElement.innerHTML = this.columnValue();
  }

  columnValue(): string {
    return this.rowValue ? this.rowValue[this.column].value : "";
  }
}
