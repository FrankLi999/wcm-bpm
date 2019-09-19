import { ResourceTypeDialog } from '../resource-type/resource-type-layout/resource-type-layout.component';

export interface JsonForm {
    repository: string;
	workspace: string;
	library: string;
    resourceType: string;
    formSchema: any;
}