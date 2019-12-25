import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';

import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';

import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatMomentDateModule } from '@angular/material-moment-adapter';
import { MatProgressBarModule} from "@angular/material";
import { MatRippleModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';

import { TranslateModule } from '@ngx-translate/core';
import { AceEditorModule } from 'ng2-ace-editor';

import {
  SharedUIModule,
  ProgressBarModule,
  SidebarModule,
  ThemeOptionsModule,
  LayoutModule,
  JsonSchemaFormModule,
  AuthGuard
} from 'bpw-components';

import { WcmAppStoreModule, ResolveGuard } from 'bpw-wcm-service';
import { WcmRendererComponent } from './entry/wcm-renderer/wcm-renderer.component';
import { WcmElementsModule } from 'bpw-wcm-elements';
import { ResourceRenderComponent } from './renderer/resource-render/resource-render.component';
const routes: Routes = [{
    path: '**',
    component: WcmRendererComponent,
    canActivate: [AuthGuard, ResolveGuard],
}];
@NgModule({
  declarations: [
    ResourceRenderComponent,
    WcmRendererComponent    
  ],
  imports : [
    RouterModule.forChild(routes),
    CommonModule,
    FormsModule, 
    ReactiveFormsModule,
    FlexLayoutModule,
    AceEditorModule,  
    TranslateModule,

    MatButtonModule,
    MatCheckboxModule,        
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    
    MatMenuModule,
    MatMomentDateModule,
    MatProgressBarModule,
    MatRippleModule,
    MatSelectModule,
    MatToolbarModule,

    SharedUIModule,
    SidebarModule,
    ProgressBarModule,
    ThemeOptionsModule,
    JsonSchemaFormModule,
    LayoutModule,
    WcmAppStoreModule,
    WcmElementsModule
  ],
  providers   : [
    ResolveGuard
  ],
  schemas: [
      CUSTOM_ELEMENTS_SCHEMA
  ],
  exports: [
    WcmRendererComponent,
    ResourceRenderComponent
  ]
})
export class WcmRendererModule {
}