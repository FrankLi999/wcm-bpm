import { WcmRepository } from './WcmRepository';
import { WcmOperation } from './WcmOperation';
import { JsonForm } from './JsonForm'; 
import { Theme } from './Theme'; 
import { RenderTemplate } from './RenderTemplate';
import { ContentAreaLayout } from './ContentAreaLayout';
import { SiteConfig } from './SiteConfig';
import { Navigation } from './Navigation';
import { SiteArea } from './SiteArea';
import { AuthoringTemplate } from './AuthoringTemplate';
import { ControlField } from './ControlField';
import { Locale } from './Locale';

export interface WcmSystem {
    wcmRepositories: WcmRepository[];
    jcrThemes: Theme[];
    operations: {[key: string]: WcmOperation[]};    
    rendertemplates: {[key:string]: RenderTemplate};
    jsonForms: {[key: string]: JsonForm};
	renderTemplates: {[key: string]: RenderTemplate};
    contentAreaLayouts: {[key: string]: ContentAreaLayout};
    
    siteConfig?: SiteConfig ;
	navigations: Navigation[];
	//Navigation id to SiteArea map
    siteAreas: {[key: string]: SiteArea};
    authoringTemplates: {[key: string]: AuthoringTemplate};
    controlFiels: ControlField[],
    langs?: string[];
    locales?: Locale[];
}