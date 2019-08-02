import { TemplateField } from './TemplateField';
export interface FormColumn {
    id: string;
    fxFlex: number;
    // formControls: TemplateField[];
    formControls: string[];
}