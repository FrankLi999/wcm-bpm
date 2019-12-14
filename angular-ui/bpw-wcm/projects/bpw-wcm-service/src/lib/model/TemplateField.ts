export interface TemplateField {
	name: string;
	title: string;
	controlName: string;
	dataType?: string; //default Text
	fieldPath?: string;
	values?: string[];
	options?: string[];
	defaultValue?: string;
	hint?: string;
	
	relationshipType?: string;
	relationshipCardinality?: string;
	valditionRegEx?: string;
	mandatory?: boolean;
	userSearchable?: boolean;
	systemIndexed?: boolean;
	showInList?: boolean;
	unique?: boolean;
}