import { HasName } from './HasName';
import { BaseFormGroup } from './BaseFormGroup';
import { TemplateField } from './TemplateField'
import { ResourceNode } from './ResourceNode'
export interface AuthoringTemplate extends ResourceNode, HasName {
	repository: string;
	workspace: string;
	library: string;
	baseResourceType?: string;
	formGroups?: BaseFormGroup[];
	formControls?: {[key: string]: TemplateField};
}