import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterModule, Routes } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatSlideToggleModule } from "@angular/material/slide-toggle";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTooltipModule } from "@angular/material/tooltip";
import { ColorPickerModule } from "ngx-color-picker";
import {
  CalendarModule as AngularCalendarModule,
  DateAdapter,
} from "angular-calendar";
import { adapterFactory } from "angular-calendar/date-adapters/date-fns";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { SharedUIModule, ConfirmDialogModule } from "bpw-common";

import { CalendarComponent } from "./calendar.component";
import { CalendarService } from "./calendar.service";
import { CalendarEventFormDialogComponent } from "./event-form/event-form.component";

const routes: Routes = [
  {
    path: "**",
    component: CalendarComponent,
    children: [],
    resolve: {
      chat: CalendarService,
    },
  },
];

@NgModule({
  declarations: [CalendarComponent, CalendarEventFormDialogComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),

    MatButtonModule,
    MatDatepickerModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSlideToggleModule,
    MatToolbarModule,
    MatTooltipModule,

    AngularCalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
    ColorPickerModule,
    PerfectScrollbarModule,
    SharedUIModule,
    ConfirmDialogModule,
  ],
  providers: [CalendarService],
})
export class CalendarModule {}
