import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LibrariesComponent } from './libraries/libraries.component';
import { LibraryComponent } from './library/library.component';
const routes: Routes = [
  {
      path       : 'edit',
      component  : LibraryComponent
  },
  {
      path       : 'list',
      component  : LibrariesComponent
  },
  {
      path       : '**',
      redirectTo: 'list'
  }
];
@NgModule({
  declarations: [LibrariesComponent, LibraryComponent],
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class ResourceLibraryModule { }
