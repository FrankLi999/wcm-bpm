import { Component, OnInit, Input, ViewChild, ElementRef } from "@angular/core";
import { RendererService } from "bpw-wcm-service";
import { ContentItem } from "bpw-wcm-service";
@Component({
  // selector: 'render-element',
  templateUrl: "./render-element.component.html",
  styleUrls: ["./render-element.component.scss"],
})
export class RenderElementComponent implements OnInit {
  @Input() element: string;
  @Input() content: string;
  contentItem: ContentItem;
  constructor(private rendererService: RendererService) {}
  @ViewChild("contentElement", { static: true }) private el: ElementRef;

  get contentElement(): string {
    return (
      this.contentItem.elements[this.element] ||
      this.contentItem.properties[this.element] ||
      ""
    );
  }
  ngOnInit() {
    if (this.content) {
      this.contentItem = this.content
        ? this.rendererService.getContentItem(this.content)
        : null;
      const content = document.getElementById(
        `resource-viewer-${this.content}`
      );
      if (content) {
        content.innerHTML = this.contentElement;
      } else {
        this.el.nativeElement.innerHTML = this.contentElement;
      }
    }
  }
}
