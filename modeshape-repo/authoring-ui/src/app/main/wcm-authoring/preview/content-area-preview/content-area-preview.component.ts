import { Component, OnInit } from '@angular/core';
import { ContentAreaLayout, ApplicationConfig, SiteArea } from '../../model';
import { WcmService } from '../../service/wcm.service';
import cloneDeep from 'lodash-es/cloneDeep';
import { RendererService } from '../renderer.service';
@Component({
  selector: 'app-content-area-preview',
  templateUrl: './content-area-preview.component.html',
  styleUrls: ['./content-area-preview.component.scss']
})
export class ContentAreaPreviewComponent implements OnInit {

  navigationId: string = 'bpm-home';
  siteArea: SiteArea;
  layout?: ContentAreaLayout;
  applicationConfig: ApplicationConfig;
  constructor(
    private rendererService: RendererService,
    private wcmService: WcmService) { }
  ngOnInit() {
    this.rendererService.clearup();
    this.wcmService.getApplicationConfig('bpwizard', 'default', 'camunda', 'bpm').subscribe(
      (applicationConfig: ApplicationConfig) => {
        if (applicationConfig) {
          this.applicationConfig = applicationConfig;
          this.siteArea = this.applicationConfig.siteAreas[this.navigationId];
          this.layout = cloneDeep(this.applicationConfig.contentAreaLayouts[this.siteArea.contentAreaLayout]||{});
          if (this.siteArea.siteAreaLayout) {
            this.layout.sidePane = cloneDeep(this.siteArea.siteAreaLayout.sidePane);
            this.layout.rows = cloneDeep(this.siteArea.siteAreaLayout.rows);
          }
        }
      },
      response => {
        console.log("getApplicationConfig call ended in error", response);
        console.log(response);
      },
      () => {
        console.log("getApplicationConfig observable is now completed.");
      }
    );
  }

  leftSidePane(): boolean {
    return this.layout && this.layout.sidePane && this.layout.sidePane.left; 
  }

  rightSidePane(): boolean {
    return this.layout && this.layout.sidePane && (!this.layout.sidePane.left); 
  }
  
  sideRenderId(viewerIndex) {
    return `s_${viewerIndex}`;
  }

  contentRenderId(rowIndex, columnIndex, viewerIndex) {
    return `c_${rowIndex}_${columnIndex}_${viewerIndex}`;
  }
}
