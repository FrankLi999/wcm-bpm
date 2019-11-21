import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ValidationRuleComponent } from './validation-rule/validation-rule.component';
import { ValidationRulesComponent } from './validation-rules/validation-rules.component';
import { ValidationRuleTreeComponent } from './validation-rule-tree/validation-rule-tree.component';

@NgModule({
  declarations: [ValidationRuleComponent, ValidationRulesComponent, ValidationRuleTreeComponent],
  imports: [
    RouterModule
  ],
  exports: [ValidationRuleComponent, ValidationRulesComponent, ValidationRuleTreeComponent]
})
export class ValidationRuleModule { }
