import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { MatSelectModule } from "@angular/material/select";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { GroupSelectionComponent } from "./group-selection/group-selection.component";
import { UserSelectionComponent } from "./user-selection/user-selection.component";
import { AclComponent } from "./acl/acl.component";

@NgModule({
  declarations: [GroupSelectionComponent, UserSelectionComponent, AclComponent],
  exports: [GroupSelectionComponent, UserSelectionComponent, AclComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ScrollingModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatIconModule,
    MatSelectModule,
    MatTableModule,
    PerfectScrollbarModule,
  ],
})
export class AclModule {}
