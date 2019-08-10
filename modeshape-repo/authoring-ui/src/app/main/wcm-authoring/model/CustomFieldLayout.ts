import { FieldLayout } from './FieldLayout';
export interface CustomFieldLayout {
    name: string;
	multiple?: boolean;
	fieldLayouts: FieldLayout[];
}