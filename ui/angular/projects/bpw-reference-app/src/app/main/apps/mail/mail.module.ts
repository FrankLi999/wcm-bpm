import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FlexLayoutModule } from "@angular/flex-layout";
import { RouterModule, Routes } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatRippleModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import { MatSelectModule } from "@angular/material/select";
import { MatToolbarModule } from "@angular/material/toolbar";
import { TranslateModule } from "@ngx-translate/core";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { SharedUIModule, SidebarModule } from "bpw-common";

import { MailService } from "./mail.service";
import { MailComponent } from "./mail.component";
import { MailListComponent } from "./mail-list/mail-list.component";
import { MailListItemComponent } from "./mail-list/mail-list-item/mail-list-item.component";
import { MailDetailsComponent } from "./mail-details/mail-details.component";
import { MailMainSidebarComponent } from "./sidebars/main/main-sidebar.component";
import { MailComposeDialogComponent } from "./dialogs/compose/compose.component";

const routes: Routes = [
  {
    path: "label/:labelHandle",
    component: MailComponent,
    resolve: {
      mail: MailService,
    },
  },
  {
    path: "label/:labelHandle/:mailId",
    component: MailComponent,
    resolve: {
      mail: MailService,
    },
  },
  {
    path: "filter/:filterHandle",
    component: MailComponent,
    resolve: {
      mail: MailService,
    },
  },
  {
    path: "filter/:filterHandle/:mailId",
    component: MailComponent,
    resolve: {
      mail: MailService,
    },
  },
  {
    path: ":folderHandle",
    component: MailComponent,
    resolve: {
      mail: MailService,
    },
  },
  {
    path: ":folderHandle/:mailId",
    component: MailComponent,
    resolve: {
      mail: MailService,
    },
  },
  {
    path: "**",
    redirectTo: "inbox",
  },
];

@NgModule({
  declarations: [
    MailComponent,
    MailListComponent,
    MailListItemComponent,
    MailDetailsComponent,
    MailMainSidebarComponent,
    MailComposeDialogComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    RouterModule.forChild(routes),

    MatButtonModule,
    MatCheckboxModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatRippleModule,
    MatSelectModule,
    MatToolbarModule,

    TranslateModule,
    PerfectScrollbarModule,
    SharedUIModule,
    SidebarModule,
  ],
  providers: [MailService],
})
export class MailModule {}
