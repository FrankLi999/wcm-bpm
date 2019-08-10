import { KeyValues } from './KeyValues';
import { SearchData } from './SearchData'
export interface Page {
    repository: string;
	workspace: string;
	nodePath: string;
	name: string;
	title?: string;
	description?: string;
	pageLayout: string;

	cacheTTL?: number;
	url?: string;
	friendlyURL?: string;

	sortOrder?: number;


	securePage?: boolean;
	showOnMenu?: boolean;
	
	metadata?: KeyValues;
	searchData?: SearchData;
}