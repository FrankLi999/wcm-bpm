import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TasklistComponent } from './tasklist/tasklist.component';



@NgModule({
  declarations: [TasklistComponent],
  imports: [
    CommonModule
  ]
})
export class TasklistModule { }
