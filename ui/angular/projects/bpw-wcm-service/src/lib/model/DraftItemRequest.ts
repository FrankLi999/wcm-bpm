export interface DraftItemRequest {
  repository: string;
  wcmPath: string;
  contentId: string;
  processInstanceId: string;
  reviewer: string;
  comment: string;
  approved: boolean;
}
