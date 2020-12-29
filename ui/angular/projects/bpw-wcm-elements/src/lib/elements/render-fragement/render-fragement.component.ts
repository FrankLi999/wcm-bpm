import { Component, OnInit, Input, ViewChild, ElementRef } from "@angular/core";
import { RendererService } from "bpw-wcm-service";
import { ContentItem } from "bpw-wcm-service";

@Component({
  // selector: "render-fragement",
  templateUrl: "./render-fragement.component.html",
  styleUrls: ["./render-fragement.component.scss"],
})
export class RenderFragementComponent implements OnInit {
  @Input() body: string;
  @Input() content: string;
  @Input() queryresultid: string;
  @Input() rowindex: number;
  constructor(private rendererService: RendererService) {}
  @ViewChild("widget", { static: true }) private el: ElementRef;

  ngOnInit(): void {
    let contextValue = null;
    if (this.content) {
      contextValue = this.content
        ? this.rendererService.getContentItem(this.content)
        : null;
    } else {
      contextValue = this.queryresultid
        ? this.rendererService.getQueryResult(this.queryresultid, this.rowindex)
        : null;
    }
    const contentNode = document.getElementById(
      `resource-viewer-${this.content}`
    );
    if (contentNode) {
      contentNode.innerHTML = this._resolveWidgetBody(contextValue);
    } else {
      this.el.nativeElement.innerHTML = this._resolveWidgetBody(contextValue);
    }
  }

  _resolveWidgetBody(contextValue) {
    var regex = /\$\{.*?\}/g;
    let matches = this.body.match(regex);
    let result = this.body;
    for (let matche of matches) {
      let data = matche
        .substring(2, matche.length - 1)
        .trim()
        .split(":");
      if (data[0] === "query") {
        result = result.replace(matche, contextValue[data[1]]);
      } else if (data[0] === "elements") {
        result = result.replace(matche, contextValue.elements[data[1]]);
      } else if (data[0] === "properties") {
        result = result.replace(matche, contextValue.properties[data[1]]);
      }
    }
    return result;
  }
}
