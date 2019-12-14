import { Component, OnInit, Input, Host, Optional } from '@angular/core';
import { RendererService } from 'bpw-wcm-service';
import { ContentItem } from 'bpw-wcm-service';
@Component({
  // selector: 'render-element',
  templateUrl: './render-element.component.html',
  styleUrls: ['./render-element.component.scss']
})
export class RenderElementComponent implements OnInit {
  @Input() element: string;
  @Input() content: string;
  contentItem: ContentItem;
  constructor(
    private rendererService: RendererService
  ) { }

  ngOnInit() {
    this.contentItem = this.content ? this.rendererService.getContentItem(this.content) : null;
  }

  contentElement(): string {
    let contentElement: string = this.contentItem.elements[this.element];
    return contentElement ? contentElement : this.contentItem.properties[this.element]
  }
}
