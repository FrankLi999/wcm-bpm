export interface ContentItem {
	repository: string;
	workspace: string;
	nodePath: string;
	authoringTemplate: string;
	contentElements: {[key: string]: string};
}