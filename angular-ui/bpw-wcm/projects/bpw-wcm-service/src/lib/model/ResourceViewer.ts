export interface ResourceViewer {
    title: string;
    renderTemplate: string;
    contentPath?: string[];
    parameterValues?: {[key: string]: string};
}