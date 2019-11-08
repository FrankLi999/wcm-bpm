import {
    Component,
    OnInit,
    Input,
    ViewChild,
    ViewContainerRef,
    Compiler,
    ComponentRef,
    ComponentFactoryResolver,
    ComponentFactory,
    NgModule,
    ModuleWithComponentFactories
  } from '@angular/core';
  import { DynamicRendererModule } from './dynamic-renderer.module';
  import { DomSanitizer } from '@angular/platform-browser';
  import { RenderTemplate, ContentItem } from '../../model';
  import { WcmService } from '../../service/wcm.service';
  import { RendererService } from '../renderer.service';
  import { WcmPluginModule } from '../wcm-plugin/wcm-plugin.module';
  //https://blog.angularindepth.com/building-an-aot-friendly-dynamic-content-outlet-in-angular-59c1a96171a
  @Component({
    selector: 'resource-renderer',
    templateUrl: './resource-renderer.component.html',
    styleUrls: ['./resource-renderer.component.scss'],
    providers:[ RendererService ]
  })
  export class ResourceRendererComponent implements OnInit {
  
    @Input() renderer;
    @Input() navigationId;
    @Input() rendererTemplate: RenderTemplate;
    @Input() content: string;
    contentItem: ContentItem;
    @ViewChild('container', { static: true, read: ViewContainerRef })
    container: ViewContainerRef;
    private componentRef: ComponentRef<{}>;
    constructor(
      private wcmService: WcmService,
      private componentFactoryResolver: ComponentFactoryResolver,
      private sanitizer: DomSanitizer,
      private rendererService: RendererService,
      private compiler: Compiler) { }
  
    ngOnInit() {
      const [repository, workspace] = this.content.split("/", 2);
      const contentItemPath = this.content.slice(`${repository}/${workspace}/`.length);
      this.sanitizer.bypassSecurityTrustHtml(this.rendererTemplate.code);
      // this.rendererTemplate.postloop = this.sanitizer.sanitize(SecurityContext.HTML, this.rendererTemplate.postloop);
      // this.rendererTemplate.preloop = this.sanitizer.sanitize(SecurityContext.HTML, this.rendererTemplate.preloop);
      this.wcmService.getContentItem(repository, workspace, contentItemPath).subscribe(
        (contentItem: ContentItem) => {
          if (contentItem) {
            this.contentItem = contentItem;
            this.rendererService.addContentItem(this.contentId(), this.contentItem);
            if (this.rendererTemplate.code) {
                this.renderDynamicContent();
            } 
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
    
    contentId(): string {
      return `${this.navigationId}-${this.renderer}`;
    }
    
    renderLayout() :boolean {
      return this.rendererService.getContentItem(this.contentId()) != undefined && this.rendererTemplate.rows != undefined;
    }

    renderDynamicContent() {
      const contentId = this.contentId();
      let metadata = {
        selector: contentId,
        template: `<ng-container contentId="${contentId}">${this.rendererTemplate.preloop}${this.rendererTemplate.code}${this.rendererTemplate.postloop}</ng-container>`
      };
  
      let factory = this.createRuntimeComponentFactorySync(this.compiler, metadata, null);
      if (this.componentRef) {
        this.componentRef.destroy();
        this.componentRef = null;
      }
      this.componentRef = this.container.createComponent(factory);    
    }
  
    private createRuntimeComponentFactorySync(compiler: Compiler, metadata: Component, componentClass: any): ComponentFactory<any> {
      const cmpClass = componentClass || class RuntimeComponent {name: string = 'ContentRender'; };
      const decoratedCmp = Component(metadata)(cmpClass);
      @NgModule({ imports: [DynamicRendererModule, WcmPluginModule], declarations: [decoratedCmp] })
      class RuntimeComponentModule { }
      let module: ModuleWithComponentFactories<any> = compiler.compileModuleAndAllComponentsSync(RuntimeComponentModule);
      return module.componentFactories.find(f => f.componentType === decoratedCmp);
    }
  }