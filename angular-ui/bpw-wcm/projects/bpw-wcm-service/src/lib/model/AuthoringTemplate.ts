import { HasName } from './HasName';
import { BaseFormGroup } from './BaseFormGroup';
import { TemplateField } from './TemplateField'
import { ResourceNode } from './ResourceNode'
import { FormRow } from './FormRow';
export interface AuthoringTemplate extends ResourceNode, HasName {
	repository: string;
	workspace: string;
	library: string;
	baseResourceType?: string;
	propertyRow: FormRow;
	elementGroups?: BaseFormGroup[];
	elements?: {[key: string]: TemplateField};
	properties?: {[key: string]: TemplateField};
	checkedOut?: boolean;
}