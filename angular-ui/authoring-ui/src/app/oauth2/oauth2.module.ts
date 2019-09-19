import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { RedirectHandlerComponent } from './redirect-handler/redirect-handler.component';
import { ProfileComponent } from './profile/profile.component';

const routes = [
  {
      path     : 'redirect',
      component: RedirectHandlerComponent
  },
  {
    path     : 'profile',
    component: ProfileComponent
  }
];
@NgModule({
  declarations: [RedirectHandlerComponent, ProfileComponent],
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class Oauth2Module { }
