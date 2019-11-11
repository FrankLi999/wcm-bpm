import { Component, OnInit, Input, Host, Optional } from '@angular/core';
import { ContentIdDirective } from '../content-id.directive';
import { RendererService } from '../../renderer.service';
import { ContentItem } from '../../../model';
@Component({
  // selector: 'render-element',
  templateUrl: './render-element.component.html',
  styleUrls: ['./render-element.component.scss']
})
export class RenderElementElementComponent implements OnInit {
  @Input() element: string;
  @Input() content: string;
  contentItem: ContentItem;
  constructor(
    private rendererService: RendererService
  ) { }

  ngOnInit() {
    this.contentItem = this.content ? this.rendererService.getContentItem(this.content) : null;
  }
}
