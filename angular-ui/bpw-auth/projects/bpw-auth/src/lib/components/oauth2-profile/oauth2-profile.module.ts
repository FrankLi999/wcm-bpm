import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProfileComponent } from './profile.component';

const routes = [
  {
    path     : 'profile',
    component: ProfileComponent
  }
];
@NgModule({
  declarations: [ProfileComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports: [ProfileComponent]
})
export class Oauth2ProfileModule { }
