import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserComponent } from './user/user.component';
import { MaterialComponent } from './material/material.component';

const routes: Routes = [
  {
    path        : 'user',
    component: UserComponent
  },
  {
    path        : 'material',
    component: MaterialComponent
  },
  {
      path      : '**',
      redirectTo: 'user'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
