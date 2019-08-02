import { BaseFormGroup } from './BaseFormGroup';
import { FormColumn } from './FormColumn';
export interface FormRow extends BaseFormGroup {
    columns: FormColumn[];
}