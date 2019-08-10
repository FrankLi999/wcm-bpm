import { TemplateField } from './TemplateField';
import { CustomFieldLayout } from './CustomFieldLayout';
export interface FormColumn {
    id: string;
    fxFlex: number;
    // formControls: TemplateField[];
    formControls: string[];
    customeFieldLayouts?: CustomFieldLayout;
}