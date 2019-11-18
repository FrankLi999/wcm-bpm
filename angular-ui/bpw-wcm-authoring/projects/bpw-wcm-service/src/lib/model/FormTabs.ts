import { BaseFormGroup } from './BaseFormGroup';
import { FormTab } from './FormTab';
export interface FormTabs extends BaseFormGroup {
	tabs: FormTab[];
}