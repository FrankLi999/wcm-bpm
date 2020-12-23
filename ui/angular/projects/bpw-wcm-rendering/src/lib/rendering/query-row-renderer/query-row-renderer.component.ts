import {
  Component,
  OnInit,
  OnDestroy,
  AfterViewInit,
  Input,
} from "@angular/core";
import { ColumnValue, RenderTemplate, RendererService } from "bpw-wcm-service";

@Component({
  selector: "query-row-renderer",
  templateUrl: "./query-row-renderer.component.html",
  styleUrls: ["./query-row-renderer.component.scss"],
})
export class QueryRowRendererComponent
  implements OnInit, OnDestroy, AfterViewInit {
  @Input() rowIndex: number;
  @Input() rendererTemplate: RenderTemplate;
  @Input() queryResultId: string;
  rowValue: { [key: string]: ColumnValue };
  constructor(private rendererService: RendererService) {}

  ngOnInit(): void {
    this.rowValue = this.rendererService.getQueryResult(
      this.queryResultId,
      this.rowIndex
    );
  }
  ngOnDestroy() {}
  ngAfterViewInit() {
    if (this.renderCode()) {
      const content = document.getElementById(`row-viewer-${this.rowId()}`);
      content.innerHTML = this.renderRow(this.rowValue);
    } else {
      this.rendererTemplate.rows.forEach((row) =>
        row.columns.forEach((column) =>
          column.elements.forEach((element) => {
            const content = document.getElementById(
              `row-viewer-${this.rowId()}-${element.name}`
            );
            content.innerHTML = this._columnValue(this.rowValue, element);
          })
        )
      );
    }
  }

  renderCode(): boolean {
    return this.rendererTemplate.code != undefined;
  }

  renderLayout(): boolean {
    return this.rendererTemplate.rows != undefined;
  }

  rowId(): string {
    return `${this.queryResultId}_${this.rowIndex}`;
  }

  renderRow(rowValue): string {
    let result = `${this.rendererTemplate.code}`.replace(
      "<query-result-column ",
      `<query-result-column queryresultid='${this.queryResultId}' rowindex='${this.rowIndex}' `
    );
    return this._resolveWidgetBody(result, rowValue);
  }

  isQuery(source: string): boolean {
    return "query" === source;
  }

  isWidget(source: string): boolean {
    return "widget" === source;
  }

  private _columnValue(rowValue, element): string {
    if (this.isQuery(element.source)) {
      return rowValue[element.name].value;
    } else {
      return this._resolveWidgetBody(element.body, rowValue);
    }
  }

  _resolveWidgetBody(content: string, rowValue) {
    var regex = /\$\{.*?\}/g;
    let matches = content.match(regex);
    let result = content;
    for (let matche of matches) {
      let data = matche
        .substring(2, matche.length - 1)
        .trim()
        .split(":");
      result = result.replace(matche, rowValue[data[1]].value);
    }
    return result;
  }
}
