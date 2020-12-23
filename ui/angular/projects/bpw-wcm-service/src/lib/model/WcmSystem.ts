import { WcmRepository } from "./WcmRepository";
import { WcmOperation } from "./WcmOperation";
import { JsonForm } from "./JsonForm";
import { Theme } from "./Theme";
import { RenderTemplate } from "./RenderTemplate";
import { ContentAreaLayout } from "./ContentAreaLayout";
import { SiteConfig } from "./SiteConfig";
import { Navigation } from "./Navigation";
import { SiteArea } from "./SiteArea";
import { AuthoringTemplate } from "./AuthoringTemplate";
import { ControlField } from "./ControlField";
import { Locale } from "./Locale";
import { QueryStatement } from "./QueryStatement";
import { Form } from "./Form";

export interface WcmSystem {
  wcmRepositories: WcmRepository[];
  jcrThemes: Theme[];
  operations: { [key: string]: WcmOperation[] };
  rendertemplates: { [key: string]: RenderTemplate };
  queryStatements: QueryStatement[];
  authoringTemplateForms: { [key: string]: JsonForm[] };
  forms: { [key: string]: JsonForm[] };
  renderTemplates: { [key: string]: RenderTemplate };
  contentAreaLayouts: { [key: string]: ContentAreaLayout };

  siteConfig?: SiteConfig;
  navigations: Navigation[];
  //Navigation id to SiteArea map
  siteAreas: { [key: string]: SiteArea };
  authoringTemplates: { [key: string]: AuthoringTemplate };
  formTemplates: { [key: string]: Form };
  controlFiels: ControlField[];
  langs?: string[];
  locales?: Locale[];
}
