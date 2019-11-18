import { BaseFormGroup } from './BaseFormGroup';
import { FormRow } from './FormRow';

export interface FormRows extends BaseFormGroup {
	rows: FormRow[];
}