import {
  Component,
  OnInit,
  OnDestroy,
  Input,
  AfterViewInit,
  ViewEncapsulation,
} from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { filter } from "rxjs/operators";
import { DomSanitizer } from "@angular/platform-browser";
import {
  RenderTemplate,
  ContentItem,
  ContentItemService,
  RendererService,
} from "bpw-wcm-service";

//https://blog.angularindepth.com/building-an-aot-friendly-dynamic-content-outlet-in-angular-59c1a96171a
@Component({
  selector: "resource-renderer",
  templateUrl: "./resource-renderer.component.html",
  styleUrls: ["./resource-renderer.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class ResourceRendererComponent
  implements OnInit, OnDestroy, AfterViewInit {
  @Input() renderer;
  @Input() siteAreaKey: string;
  @Input() rendererTemplate: RenderTemplate;
  @Input() contentPath: string;
  @Input() repository: string;
  @Input() workspace: string;
  @Input() contentPathIndex: number;
  contentItemChange = new BehaviorSubject<ContentItem>(null);
  contentItem: ContentItem = null;

  constructor(
    private contentItemService: ContentItemService,
    private sanitizer: DomSanitizer,
    private rendererService: RendererService
  ) {
  }

  ngOnInit() {
    this.sanitizer.bypassSecurityTrustHtml(this.rendererTemplate.code);
    this.contentItemService
      .getContentItem(this.repository, this.workspace, this.contentPath)
      .pipe(filter((item) => !!item))
      .subscribe((contentItem: ContentItem) => {
        this.contentItem = contentItem;
        this.rendererService.addContentItem(this.contentId(), this.contentItem);
        this.contentItemChange.next(this.contentItem);
      });
  }

  ngAfterViewInit() {
    this.contentItemChange.subscribe((contentItem) => {
      if (contentItem != null) {
        if (this.renderCode()) {
          const content = document.getElementById(
            `resource-viewer-${this.contentId()}`
          );
          content.innerHTML = this.elementRenderContent(contentItem);
        } else {
          this.rendererTemplate.rows.forEach((row) =>
            row.columns.forEach((column) =>
              column.elements.forEach((element) => {
                const content = document.getElementById(
                  `resource-viewer-${this.contentId()}-${element.name}`
                );
                content.innerHTML = this._contentElement(
                  contentItem,
                  element.name
                );
              })
            )
          );
        }
      }
    });
  }

  ngOnDestroy() {
    this.contentItemChange.unsubscribe();
  }
  contentId(): string {
    return `${this.siteAreaKey}_${this.renderer}_${this.contentPathIndex}`;
  }

  renderCode(): boolean {
    return this.rendererTemplate.code != undefined;
  }

  renderLayout(): boolean {
    return this.rendererTemplate.rows != undefined;
  }

  elementRenderContent(contentItem: ContentItem): string {
    let result = `${this.rendererTemplate.code}`
      .replace(
        "<render-element ",
        `<render-element content='${this.contentId()}' `
      )
      .replace(
        "<render-property ",
        `<render-property content='${this.contentId()}' `
      );

    return this._resolveWidgetBody(result);
  }

  isElement(source: string): boolean {
    return "element" === source;
  }

  isProperty(source: string): boolean {
    return "property" === source;
  }

  isWidget(source: string): boolean {
    return "widget" === source;
  }

  private _contentElement(contentItem, elementName: string): string {
    return (
      contentItem.elements[elementName] ||
      contentItem.properties[elementName] ||
      ""
    );
  }

  _resolveWidgetBody(content: string) {
    var regex = /\$\{.*?\}/g;
    let matches = content.match(regex);

    let result = content;
    if (matches) {
      for (let matche of matches) {
        let data = matche
          .substring(2, matche.length - 1)
          .trim()
          .split(":");
        if (data[0] === "elements") {
          result = result.replace(matche, this.contentItem.elements[data[1]]);
        } else if (data[0] === "properties") {
          result = result.replace(matche, this.contentItem.properties[data[1]]);
        }
      }
    }
    return result;
  }
}
