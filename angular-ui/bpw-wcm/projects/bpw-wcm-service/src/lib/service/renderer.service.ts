import { Injectable } from '@angular/core';
import { ContentItem } from '../model/ContentItem';

@Injectable({
  providedIn: 'root'
})
export class RendererService {
  contentItems:{[key: string]: ContentItem} = {}
  constructor() { }

  clearup() {
    for (var key in this.contentItems) {
      // this check can be safely omitted in modern JS engines
      // if (obj.hasOwnProperty(key))
        delete this.contentItems[key];
    }
  }

  addContentItem(id: string, contentItem: ContentItem) {
    this.contentItems[id] = contentItem;
  }

  getContentItem(id: string) {
    return this.contentItems[id];
  }
}
