import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Routes, RouterModule } from "@angular/router";
import { MainComponent } from "./main/main.component";
import { PrivacyPolicyComponent } from "./privacy-policy/privacy-policy.component";
import { GridComponent } from "./grid/grid.component";
import { HeaderComponent } from "./header/header.component";
import { NavComponent } from "./nav/nav.component";
import { AsideComponent } from "./aside/aside.component";
import { FooterComponent } from "./footer/footer.component";
import { TopNavComponent } from './top-nav/top-nav.component';
import { ToolbarComponent } from './toolbar/toolbar.component';

const routes: Routes = [
  {
    path: "",
    component: GridComponent,
    children: [
      { path: "home", component: MainComponent },
      { path: "privacy-policy", component: PrivacyPolicyComponent },
    ],
  },
];

@NgModule({
  declarations: [
    GridComponent,
    HeaderComponent,
    NavComponent,
    MainComponent,
    AsideComponent,
    FooterComponent,
    PrivacyPolicyComponent,
    TopNavComponent,
    ToolbarComponent,
  ],
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class GridModule {}
