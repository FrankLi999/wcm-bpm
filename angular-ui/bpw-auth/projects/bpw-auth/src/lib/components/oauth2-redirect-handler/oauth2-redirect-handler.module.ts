import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { RedirectHandlerComponent } from './redirect-handler.component';

const routes = [
  {
      path     : 'redirect',
      component: RedirectHandlerComponent
  }
];
@NgModule({
  declarations: [RedirectHandlerComponent],
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [RedirectHandlerComponent]
})
export class Oauth2RedirectHandlerModule { }
