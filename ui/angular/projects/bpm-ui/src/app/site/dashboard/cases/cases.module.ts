import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CasesComponent } from './cases/cases.component';
import { CaseDefinitionRuntimeComponent } from './case-definition-runtime/case-definition-runtime.component';
import { CaseInstanceComponent } from './case-instance/case-instance.component';



@NgModule({
  declarations: [CasesComponent, CaseDefinitionRuntimeComponent, CaseInstanceComponent],
  imports: [
    CommonModule
  ]
})
export class CasesModule { }
