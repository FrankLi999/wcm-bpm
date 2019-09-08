import { Component, OnInit, Input, Host } from '@angular/core';
import { ContentIdDirective } from '../content-id.directive';
import { RendererService } from '../renderer.service';
import { ContentItem } from '../../model';
@Component({
  selector: 'render-element',
  templateUrl: './render-element.component.html',
  styleUrls: ['./render-element.component.scss']
})
export class RenderElementComponent implements OnInit {

  @Input() elementName: string;
  contentItem: ContentItem;
  constructor(
    private rendererService: RendererService,
    @Host() private contentIdDirective: ContentIdDirective) { }

  ngOnInit() {
    this.contentItem = this.rendererService.getContentItem(this.contentIdDirective.contentId);
  }
}
