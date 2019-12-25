import { NgModule } from '@angular/core';
import { Oauth2ProfileModule } from './oauth2-profile/oauth2-profile.module';
import { Oauth2RedirectHandlerModule } from './oauth2-redirect-handler/oauth2-redirect-handler.module';

// const routes = [
//   {
//     path     : 'profile',
//     component: ProfileComponent,
//     canActivate: [AuthGuard]
//   },
//   {
//     path     : 'redirect',
//     component: RedirectHandlerComponent
//   }
// ];
@NgModule({
  imports: [
    Oauth2ProfileModule,
    Oauth2RedirectHandlerModule
  ],
  exports: [
    Oauth2ProfileModule,
    Oauth2RedirectHandlerModule
  ]
})
export class OAuth2Module { }
