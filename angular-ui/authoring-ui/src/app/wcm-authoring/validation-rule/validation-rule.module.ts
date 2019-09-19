import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ValidationRuleComponent } from './validation-rule/validation-rule.component';
import { ValidationRulesComponent } from './validation-rules/validation-rules.component';
const routes: Routes = [
  {
      path       : 'validation-rule/edit',
      component  : ValidationRuleComponent
  },
  {
      path       : 'validation-rule/list',
      component  : ValidationRulesComponent
  }
];
@NgModule({
  declarations: [ValidationRuleComponent, ValidationRulesComponent],
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class ValidationRuleModule { }
