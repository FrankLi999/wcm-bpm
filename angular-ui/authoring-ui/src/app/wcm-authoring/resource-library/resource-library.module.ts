import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LibrariesComponent } from './libraries/libraries.component';
import { LibraryComponent } from './library/library.component';
import * as fromGuards from '../store/guards';
import { AuthGuard } from 'bpw-auth';
const routes: Routes = [
  {
      path       : 'resource-library/edit',
      component  : LibraryComponent,
      canActivate: [AuthGuard, fromGuards.ResolveGuard]
  },
  {
      path       : 'resource-library/list',
      component  : LibrariesComponent,
      canActivate: [AuthGuard, fromGuards.ResolveGuard]
  }
];
@NgModule({
  declarations: [LibrariesComponent, LibraryComponent],
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class ResourceLibraryModule { }
