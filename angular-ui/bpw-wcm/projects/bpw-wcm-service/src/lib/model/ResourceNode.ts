export interface ResourceNode {
    title?: string;
	description?: string;
    workflow?: string[];
    workflowStage?: string;
	categories?: string[];
    publishDate?: Date;
    expireDate?: Date;
}