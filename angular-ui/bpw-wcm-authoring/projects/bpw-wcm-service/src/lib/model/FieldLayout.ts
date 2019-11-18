export interface FieldLayout {
    name: string;
    key: string;
	title: string;
	multiple: boolean;
	items: string;
    fieldLayouts?: FieldLayout[];
}