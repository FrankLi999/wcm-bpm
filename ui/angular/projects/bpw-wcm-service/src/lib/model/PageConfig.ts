import { SiteConfig } from './SiteConfig';
import { Navigation } from './Navigation';
import { Locale } from './Locale';

export interface PageConfig {
  siteConfig: SiteConfig;
  navigations: Navigation[];
  langs?: string[];
  locales?: Locale[];
}