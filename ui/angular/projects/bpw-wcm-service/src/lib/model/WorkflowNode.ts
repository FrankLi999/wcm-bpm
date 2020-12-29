import { ResourceNode } from "./ResourceNode";

export interface WorkflowNode extends ResourceNode {
  workflow?: string;
  workflowStage?: string;
  publishDate?: Date;
  expireDate?: Date;
  processInstanceId?: string;
  reviewer: string;
  editor: string;
}
