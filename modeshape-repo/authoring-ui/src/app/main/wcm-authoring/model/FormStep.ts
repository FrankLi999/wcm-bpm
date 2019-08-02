import { BaseFormGroup } from './BaseFormGroup';
export interface FormStep {
    stepName: string;
    formGroups: BaseFormGroup[];
}