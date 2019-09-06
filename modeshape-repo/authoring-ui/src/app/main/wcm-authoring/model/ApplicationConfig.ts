import {SiteConfig} from './SiteConfig';
import {Navigation} from './Navigation';
import {JsonForm} from './JsonForm';
export interface ApplicationConfig {
    siteConfig: SiteConfig ;
	navigations: Navigation[];
	jsonForms: JsonForm[];
}