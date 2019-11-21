import { Component, OnInit, Input, Host, Optional } from '@angular/core';
import { ContentIdDirective } from '../content-id.directive';
import { RendererService } from '../../renderer.service';
import { ContentItem } from 'bpw-wcm-service';
@Component({
  selector: 'render-element1',
  templateUrl: './render-element.component.html',
  styleUrls: ['./render-element.component.scss']
})
export class RenderElementComponent implements OnInit {

  @Input() element: string;
  contentItem: ContentItem;
  constructor(
    private rendererService: RendererService,
    @Host() private contentIdDirective: ContentIdDirective
  ) { }

  ngOnInit() {
    this.contentItem = this.contentIdDirective ? this.rendererService.getContentItem(this.contentIdDirective.contentId) : null;
  }
}
