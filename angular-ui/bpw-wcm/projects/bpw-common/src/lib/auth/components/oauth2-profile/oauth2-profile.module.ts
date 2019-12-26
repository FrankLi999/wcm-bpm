import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { SharedUIModule } from '../../../common/shared-ui.module';
import { RouterModule } from '@angular/router';
import { ProfileComponent } from './profile.component';
import { AuthGuard } from '../../store/guards/auth.guard';

const routes = [
  {
    path     : 'profile',
    component: ProfileComponent,
    canActivate: [AuthGuard]
  }
];
@NgModule({
  declarations: [ProfileComponent],
  imports: [
    CommonModule,
    FlexLayoutModule,
    SharedUIModule,
    RouterModule.forChild(routes)
  ],
  exports: [ProfileComponent]
})
export class Oauth2ProfileModule { }
