import { HasName } from './HasName';
import { KeyValues } from './KeyValues';
import { SearchData } from './SearchData'
export interface SiteArea extends HasName {
	repository: string;
	workspace: string;
	nodePath: string;
	name: string;
	title?: string;
	description?: string;
	url?: string;
	sorderOrder?: number;
	friendlyURL?: string;
	showOnMenu?: boolean;
	defaultContent?: string;
	allowedFileExtension?: string;
	allowedArtifactTypes?: string;

	pageLayout: string;
	cacheTTL?: number;
	securePage?: boolean;
	metadata?: KeyValues;
	searchData?: SearchData;
}