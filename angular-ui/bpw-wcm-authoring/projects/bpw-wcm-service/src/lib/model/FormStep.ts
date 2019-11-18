import { BaseFormGroup } from './BaseFormGroup';
export interface FormStep {
    stepName: string;
    stepTitle?: string;
    formGroups: BaseFormGroup[];
}