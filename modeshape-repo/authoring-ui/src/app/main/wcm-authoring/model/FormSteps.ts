import { BaseFormGroup } from './BaseFormGroup';
import { FormStep } from './FormStep';
export interface FormSteps extends BaseFormGroup {
	steps: FormStep[];
}