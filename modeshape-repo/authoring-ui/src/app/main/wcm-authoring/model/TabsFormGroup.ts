import { BaseFormGroup } from './BaseFormGroup';
import { FormTab } from './FormTab';
export interface TabsFormGroup extends BaseFormGroup {
	tabs: FormTab[];
}