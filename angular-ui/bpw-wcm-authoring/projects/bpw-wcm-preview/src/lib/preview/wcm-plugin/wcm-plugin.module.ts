import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RenderElementComponent } from './render-element/render-element.component';
import { ContentIdDirective } from './content-id.directive';

@NgModule({
  declarations: [
    RenderElementComponent,
    ContentIdDirective
  ],
  imports: [
    CommonModule
  ],
  exports: [
    RenderElementComponent,
    ContentIdDirective
  ]
})
export class WcmPluginModule { }