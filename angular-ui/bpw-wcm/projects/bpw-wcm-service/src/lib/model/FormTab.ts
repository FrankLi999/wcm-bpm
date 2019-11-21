import { BaseFormGroup } from './BaseFormGroup';
export interface FormTab {
    tabName: string;
    tabTitle?: string;
    formGroups: BaseFormGroup[];
}