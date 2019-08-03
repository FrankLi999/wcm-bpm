import { BaseFormGroup } from './BaseFormGroup';
import { FormStep } from './FormStep';
export interface StepsFormGroup extends BaseFormGroup {
	steps: FormStep[];
}