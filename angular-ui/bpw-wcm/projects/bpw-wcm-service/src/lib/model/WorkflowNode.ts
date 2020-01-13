import { ResourceNode } from './ResourceNode';

export interface workflowNode extends ResourceNode {
    workflow?: string;
    workflowStage?: string;
    publishDate?: Date;
    expireDate?: Date;
}