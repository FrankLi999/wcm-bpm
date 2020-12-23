import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { FlexLayoutModule } from "@angular/flex-layout";
import { CdkTableModule } from "@angular/cdk/table";
import { CdkTreeModule } from "@angular/cdk/tree";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatRippleModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSelectModule } from "@angular/material/select";
import { MatSortModule } from "@angular/material/sort";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTableModule } from "@angular/material/table";
import { MatTabsModule } from "@angular/material/tabs";
import { MatTreeModule } from "@angular/material/tree";
import { TranslateModule } from "@ngx-translate/core";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";

import { SharedUIModule, SidebarModule } from "bpw-common";
import { JsonSchemaFormModule } from "@bpw/ajsf-core";
import { MaterialDesignFrameworkModule } from "@bpw/ajsf-material";
import { ComponentModule } from "../components/component.module";

import { ContentReviewTasksComponent } from "./content-review-tasks/content-review-tasks.component";
import { ContentApprovalComponent } from "./content-approval/content-approval.component";
import { RejectDraftComponent } from "./reject-draft/reject-draft.component";
import { DraftItemTreeComponent } from "./draft-item-tree/draft-item-tree.component";

@NgModule({
  declarations: [
    ContentReviewTasksComponent,
    ContentApprovalComponent,
    RejectDraftComponent,
    DraftItemTreeComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    FlexLayoutModule,
    CdkTableModule,
    CdkTreeModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDialogModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatRippleModule,
    MatSelectModule,
    MatSortModule,
    MatTabsModule,
    MatTableModule,
    MatToolbarModule,
    MatTreeModule,

    PerfectScrollbarModule,
    TranslateModule.forChild({
      extend: true,
    }),

    SharedUIModule,
    SidebarModule,
    JsonSchemaFormModule,
    MaterialDesignFrameworkModule,
    ComponentModule,
  ],
})
export class AuthoringTaskModule {}
