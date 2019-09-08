import {SiteConfig} from './SiteConfig';
import {Navigation} from './Navigation';
import {JsonForm} from './JsonForm';
import {RenderTemplate} from './RenderTemplate';
import {SiteArea} from './SiteArea';
import {ContentAreaLayout} from './ContentAreaLayout';

export interface ApplicationConfig {
    siteConfig: SiteConfig ;
	navigations: Navigation[];
	jsonForms: {[key: string]: JsonForm};
	renderTemplates: {[key: string]: RenderTemplate};
	//Navigation id to SiteArea map
	siteAreas: {[key: string]: SiteArea};
	contentAreaLayouts: {[key: string]: ContentAreaLayout};
}