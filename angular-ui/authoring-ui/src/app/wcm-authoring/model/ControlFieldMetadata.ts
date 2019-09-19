import { HasName } from './HasName';
export interface ControlFieldMetadata extends HasName {
	title?: string;
	controlType?: string;
	hintText?: string;
	required?: boolean;
	readonly?: boolean;
	selectOptions?: string[];
}