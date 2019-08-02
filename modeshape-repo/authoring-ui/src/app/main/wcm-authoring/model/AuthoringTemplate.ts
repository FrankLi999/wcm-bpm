import { HasName } from './HasName';
import { BaseFormGroup } from './BaseFormGroup';
import { TemplateField } from './TemplateField'
export interface AuthoringTemplate extends HasName {
	title?: string;
	description?: string;
	workflow?: string[];
	categories?: string[];
	baseResourceType?: string;
	publishDate?: Date;
	formGroups?: BaseFormGroup[];
	formControls?: {[key: string]: TemplateField};
}