export interface TemplateField {
	name: string;
    title: string;
	controlName: string;
	values: string[];
	options: string[];
	defaultValue: string;
	hint: string;
	dataType: string; //default Text
	relationshipType: string;
	relationshipCardinality: string;
	valditionRegEx: string;
	mandatory: boolean;
	userSearchable: boolean;
	systemIndexed: boolean;
	showInList: boolean;
	unique: boolean;
}