import { NgModule } from '@angular/core'
import { CommonModule } from '@angular/common';
import { RouterModule,  Routes } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';
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
import { FuseSharedModule, FuseSidebarModule } from 'bpw-components';

import { ContentAreaPreviewComponent } from './content-area-preview/content-area-preview.component';
import { ResourceRendererComponent } from './resource-renderer/resource-renderer.component';
import { WcmPluginModule } from './wcm-plugin/wcm-plugin.module';
import * as fromGuards from '../store/guards';
import { AuthGuard } from 'bpw-store';
import { KeepHtmlPipe } from './keep-html.pipe';

const routes: Routes = [
  {
    path     : 'preview',
    component: ContentAreaPreviewComponent,
    canActivate: [AuthGuard, fromGuards.ResolveGuard]       
  }
];
@NgModule({
  declarations: [
    ContentAreaPreviewComponent, 
    ResourceRendererComponent,
    KeepHtmlPipe
  ],
  imports: [
    RouterModule.forChild(routes),
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
    HttpClientModule,
    TranslateModule,
    FuseSharedModule,
    FuseSidebarModule,
    WcmPluginModule
  ],  
  exports: [
    ResourceRendererComponent,
    KeepHtmlPipe
  ]
})
export class PreviewModule { 
}