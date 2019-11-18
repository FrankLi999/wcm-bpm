import { FieldLayout } from './FieldLayout';
export interface FormColumn {
    id: string;
    fxFlex: number;
    // formControls: TemplateField[];
    formControls: string[];
    fieldLayouts?: FieldLayout;
}