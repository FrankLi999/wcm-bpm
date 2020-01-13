export interface ContentItem {
	id: string;
	name?: string;
	categories?: string[];
	repository: string;
	workspace: string;
	nodePath: string;
	lifeCycleStage: string;
	locked: boolean;
	checkedOut: boolean;
	authoringTemplate: string;
	elements: {[key: string]: string};
	properties: {[key: string]: string};
}