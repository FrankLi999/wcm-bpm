import { HasName } from './HasName';
import { KeyValues } from './KeyValues';
import { SearchData } from './SearchData'
import { SiteAreaLayout } from './SiteAreaLayout';
import { NavigationBadge } from './NavigationBadge';

export interface SiteArea extends HasName {
	repository: string;
	workspace: string;
	nodePath?: string;
	name: string;
	title?: string;
	description?: string;
	url?: string;
	sorderOrder?: number;
	friendlyURL?: string;
	showOnMenu?: boolean;
	contentPath?: string;
	allowedFileExtension?: string;
	allowedArtifactTypes?: string;

	contentAreaLayout?: string;
	cacheTTL?: number;
	securePage?: boolean;
	metadata?: KeyValues;
	searchData?: SearchData;
	siteAreaLayout?: SiteAreaLayout;

	navigationId?: string;
	navigationType?: string;
	function?: string;
	translate?: string;
	icon?: string;
	classes?:string;
	exactMatch?: boolean;
	externalUrl?: boolean;
	openInNewTab?: boolean;
	badge?: NavigationBadge;
}