import { BaseFormGroup } from './BaseFormGroup';
import { FormRow } from './FormRow';

export interface RowsFormGroup extends BaseFormGroup {
	rows: FormRow[];
}