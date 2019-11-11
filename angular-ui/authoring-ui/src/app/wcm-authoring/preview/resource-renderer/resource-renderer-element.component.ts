import {
  Component,
  OnInit,
  OnDestroy,
  Input,
  AfterViewInit,
  ComponentRef
} from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { DomSanitizer } from '@angular/platform-browser';
import { RenderTemplate, ContentItem } from '../../model';
import { WcmService } from '../../service/wcm.service';
import { RendererService } from '../renderer.service';
import { RouterLinkWithHref } from '@angular/router';
import { CdkColumnDef } from '@angular/cdk/table';

//https://blog.angularindepth.com/building-an-aot-friendly-dynamic-content-outlet-in-angular-59c1a96171a
@Component({
  selector: 'resource-renderer',
  templateUrl: './resource-renderer-element.component.html',
  styleUrls: ['./resource-renderer-element.component.scss']
})
export class ResourceRendererElementComponent implements OnInit, OnDestroy, AfterViewInit {

  @Input() renderer;
  @Input() navigationId;
  @Input() rendererTemplate: RenderTemplate;
  @Input() content: string;
  contentItemChange = new BehaviorSubject<ContentItem>(null);
  contentItem: ContentItem = null;
  private componentRef: ComponentRef<{}>;
  constructor(
    private wcmService: WcmService,
    private sanitizer: DomSanitizer,
    private rendererService: RendererService) { }

  ngOnInit() {
    const [repository, workspace] = this.content.split("/", 2);
    const contentItemPath = this.content.slice(`${repository}/${workspace}/`.length);
    this.sanitizer.bypassSecurityTrustHtml(this.rendererTemplate.code);
    this.wcmService.getContentItem(repository, workspace, contentItemPath).subscribe(
      (contentItem: ContentItem) => {
        if (contentItem) {
          this.contentItem = contentItem;
          this.rendererService.addContentItem(this.contentId(), this.contentItem);
          this.contentItemChange.next(this.contentItem);
        }
      },
      response => {
        console.log("getContentItem call ended in error", response);
        console.log(response);
      },
      () => {
        console.log("getContentItem observable is now completed.");
      }
    );
  }
  
  ngAfterViewInit() {
    this.contentItemChange.subscribe(contentItem => {
      if (contentItem != null) {
        if( this.renderCode() ) {
          const content = document.getElementById(`resource-viewer-${this.contentId()}`);
          content.innerHTML = this.elementRenderContent(contentItem);
        } else {
          this.rendererTemplate.rows.forEach(row => row.columns.forEach(column => column.elements.forEach(
            element => {
              const content = document.getElementById(`resource-viewer-${this.contentId()}-${element.name}`);
              content.innerHTML = `<render-element content="${this.contentId()}" element="${element.name}"></render-element>`;
            }
          )))
        }
      } 
    });
    
  }

  ngOnDestroy() {
    this.contentItemChange.unsubscribe();
  }
  contentId(): string {
    return `${this.navigationId}_${this.renderer}`;
  }

  renderCode() :boolean {
    return this.rendererTemplate.code != undefined;
  }

  renderLayout() :boolean {
    return this.rendererTemplate.rows != undefined;
  }

  elementRenderContent(contentItem: ContentItem): string {
    return `${this.rendererTemplate.preloop}${this.rendererTemplate.code}${this.rendererTemplate.postloop}`.replace('<render-element ', `<render-element content='${this.contentId()}' `);
  }
}
