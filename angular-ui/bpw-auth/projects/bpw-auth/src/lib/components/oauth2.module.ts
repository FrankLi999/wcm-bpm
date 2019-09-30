import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProfileComponent } from './oauth2-profile/profile.component';
import { AuthGuard } from 'bpw-store';
import { RedirectHandlerComponent } from './oauth2-redirect-handler/redirect-handler.component';

const routes = [
  {
    path     : 'profile',
    component: ProfileComponent,
    canActivate: [AuthGuard]
  },
  {
    path     : 'redirect',
    component: RedirectHandlerComponent
  }
];
@NgModule({
  declarations: [
    ProfileComponent,
    RedirectHandlerComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    ProfileComponent,
    RedirectHandlerComponent
  ]
})
export class OAuth2Module { }
