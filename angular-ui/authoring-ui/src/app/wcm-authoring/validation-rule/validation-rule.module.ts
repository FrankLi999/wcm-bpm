import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ValidationRuleComponent } from './validation-rule/validation-rule.component';
import { ValidationRulesComponent } from './validation-rules/validation-rules.component';
import * as fromGuards from '../store/guards';
import { AuthGuard } from 'bpw-auth';

const routes: Routes = [
  {
      path       : 'validation-rule/edit',
      component  : ValidationRuleComponent,
      canActivate: [AuthGuard, fromGuards.ResolveGuard]
  },
  {
      path       : 'validation-rule/list',
      component  : ValidationRulesComponent,
      canActivate: [AuthGuard, fromGuards.ResolveGuard]
  }
];
@NgModule({
  declarations: [ValidationRuleComponent, ValidationRulesComponent],
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class ValidationRuleModule { }
