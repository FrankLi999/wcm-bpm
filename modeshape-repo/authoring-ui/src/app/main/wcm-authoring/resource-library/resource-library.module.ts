import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LibrariesComponent } from './libraries/libraries.component';
import { LibraryComponent } from './library/library.component';
const routes: Routes = [
  {
      path       : 'resource-library/edit',
      component  : LibraryComponent
  },
  {
      path       : 'resource-library/list',
      component  : LibrariesComponent
  }
];
@NgModule({
  declarations: [LibrariesComponent, LibraryComponent],
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class ResourceLibraryModule { }
