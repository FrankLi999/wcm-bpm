import { Injectable } from "@angular/core";
import { ContentItem } from "../model/ContentItem";
import { QueryResult } from "../model/QueryResult";

@Injectable({
  providedIn: "root",
})
export class RendererService {
  contentItems: { [key: string]: ContentItem } = {};
  queryResults: { [key: string]: QueryResult } = {};
  constructor() {}

  clearup() {
    for (var key in this.contentItems) {
      // this check can be safely omitted in modern JS engines
      // if (obj.hasOwnProperty(key))
      delete this.contentItems[key];
    }

    for (var key in this.queryResults) {
      delete this.queryResults[key];
    }
  }

  addContentItem(id: string, contentItem: ContentItem) {
    this.contentItems[id] = contentItem;
  }

  addQueryResult(id: string, queryResult: QueryResult) {
    this.queryResults[id] = queryResult;
  }

  getContentItem(id: string) {
    return this.contentItems[id];
  }

  getQueryResult(id: string, rowIndex: number) {
    return this.queryResults[id].rows[rowIndex];
  }
}
