import { Route } from "@angular/router";

import { DemoComponent } from "./demo.component";
import { RegistrationComponent } from "./registration/registration.component";
import { ChipComponent } from "./chip/chip/chip.component";
import { TreeviewDropdownComponent } from "./chip/treeview-dropdown/treeview-dropdown.component";

export const routes: Route[] = [
  { path: "", component: DemoComponent },
  { path: "registration", component: RegistrationComponent },
  { path: "chip", component: ChipComponent },
  { path: "tree", component: TreeviewDropdownComponent },
  { path: "**", component: DemoComponent },
];
