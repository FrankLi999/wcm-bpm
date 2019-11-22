import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContentIdDirective } from './content-id.directive';

@NgModule({
  declarations: [
    ContentIdDirective
  ],
  imports: [
    CommonModule
  ],
  exports: [
    ContentIdDirective
  ]
})
export class WcmPluginModule { }