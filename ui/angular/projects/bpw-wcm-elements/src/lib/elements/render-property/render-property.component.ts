import { Component, OnInit, Input } from "@angular/core";
import { RendererService } from "bpw-wcm-service";
import { ContentItem } from "bpw-wcm-service";
@Component({
  //selector: "render-property",
  templateUrl: "./render-property.component.html",
  styleUrls: ["./render-property.component.scss"],
})
export class RenderPropertyComponent implements OnInit {
  @Input() property: string;
  @Input() content: string;
  contentItem: ContentItem;
  constructor(private rendererService: RendererService) {}

  get contentProperty(): string {
    return this.contentItem?.properties[this.property] || "";
  }

  ngOnInit() {
    this.contentItem = this.content
      ? this.rendererService.getContentItem(this.content)
      : null;
  }
}
