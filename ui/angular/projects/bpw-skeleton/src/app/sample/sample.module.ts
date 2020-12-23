import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { TranslateModule } from "@ngx-translate/core";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { SharedUIModule } from "bpw-common";

import { SampleComponent } from "./sample.component";

const routes = [
  {
    path: "sample",
    component: SampleComponent,
  },
];

@NgModule({
  declarations: [SampleComponent],
  imports: [
    RouterModule.forChild(routes),

    TranslateModule,
    PerfectScrollbarModule,
    SharedUIModule,
  ],
  exports: [SampleComponent],
})
export class SampleModule {}
