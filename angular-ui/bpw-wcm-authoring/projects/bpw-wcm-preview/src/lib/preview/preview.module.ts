import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Injector } from '@angular/core'
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRippleModule } from '@angular/material/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSortModule } from '@angular/material/sort';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';
import { MatTreeModule } from '@angular/material/tree';
import { TranslateModule } from '@ngx-translate/core';

import { createCustomElement } from '@angular/elements';

import { FuseSharedModule, FuseSidebarModule } from 'bpw-components';

import { ContentAreaPreviewComponent } from './content-area-preview/content-area-preview.component';
import { ResourceRendererComponent } from './resource-renderer/resource-renderer.component';
import { ResourceRendererElementComponent } from './resource-renderer/resource-renderer-element.component';
import { RenderElementElementComponent } from './wcm-plugin/render-element/render-element-element.component';
import { WcmPluginModule } from './wcm-plugin/wcm-plugin.module';
import { KeepHtmlPipe } from './keep-html.pipe';

@NgModule({
  declarations: [
    ContentAreaPreviewComponent, 
    ResourceRendererComponent,
    ResourceRendererElementComponent,
    RenderElementElementComponent,
    KeepHtmlPipe
  ],
  imports: [
    RouterModule,
    CommonModule,
    CdkTableModule,
    CdkTreeModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDialogModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatPaginatorModule,
    MatRippleModule,
    MatSelectModule,
    MatSortModule,
    MatTableModule,
    MatToolbarModule,
    MatTreeModule,
   
    FlexLayoutModule,
    // HttpClientModule,
    TranslateModule,
    FuseSharedModule,
    FuseSidebarModule,
    WcmPluginModule
  ],  
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  exports: [
    ContentAreaPreviewComponent, 
    ResourceRendererComponent,
    ResourceRendererElementComponent,
    RenderElementElementComponent,
    KeepHtmlPipe
  ],
  entryComponents: [
    RenderElementElementComponent
  ]
})
export class PreviewModule {
  constructor(private injector: Injector) {  
      const renderElementElementComponent = createCustomElement(RenderElementElementComponent, { injector: injector });
      customElements.define('render-element', renderElementElementComponent);
  }
}