import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { RedirectHandlerComponent } from './redirect-handler.component';
import { RestClientModule } from 'bpw-rest-client';
const routes = [
  {
      path     : 'redirect',
      component: RedirectHandlerComponent
  }
];
@NgModule({
  declarations: [RedirectHandlerComponent],
  imports: [
    RestClientModule,
    RouterModule.forChild(routes)
  ],
  exports: [RedirectHandlerComponent]
})
export class Oauth2RedirectHandlerModule { }
