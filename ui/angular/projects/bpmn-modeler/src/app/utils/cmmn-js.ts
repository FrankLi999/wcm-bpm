import * as _CmmnModeler from "cmmn-js/dist/cmmn-modeler.production.min.js";
import * as _CmmnViewer from "cmmn-js/dist/cmmn-viewer.production.min.js";
import * as _CmmnNavigatedViewer from "cmmn-js/dist/cmmn-navigated-viewer.production.min.js";
import * as _cmmnPropertiesPanelModule from "cmmn-js-properties-panel";
import * as _cmmnPropertiesProviderModule from "cmmn-js-properties-panel/lib/provider/camunda";
import * as _cmmnPropertiesProvider from "cmmn-js-properties-panel/lib/provider/cmmn";
import * as _cmmnEntryFactory from "cmmn-js-properties-panel/lib/factory/EntryFactory";
import _cmmnPaletteProvider from "cmmn-js/lib/features/palette/PaletteProvider";
import * as _camundaCmmnModdleDescriptor from "camunda-cmmn-moddle/resources/camunda.json";
export const CmmnInjectionNames = {
  eventBus: "eventBus",
  cmmnFactory: "cmmnFactory",
  elementRegistry: "elementRegistry",
  translate: "translate",
  propertiesProvider: "propertiesProvider",
  cmmnPropertiesProvider: "cmmnPropertiesProvider",
  paletteProvider: "paletteProvider",
  originalPaletteProvider: "originalPaletteProvider",
};

export const CmmnModeler = _CmmnModeler;
export const CmmnViewer = _CmmnViewer;
export const CmmnNavigatedViewer = _CmmnNavigatedViewer;
export const cmmnPropertiesPanelModule = _cmmnPropertiesPanelModule;
export const cmmnPropertiesProviderModule = _cmmnPropertiesProviderModule;
export const cmmnEntryFactory = _cmmnEntryFactory;
export const OriginalCmmnPaletteProvider = _cmmnPaletteProvider;
export const OriginalCmmnPropertiesProvider = _cmmnPropertiesProvider;
export const camundaCmmnModdleDescriptor = _camundaCmmnModdleDescriptor;

export interface ICmmnPaletteProvider {
  getPaletteEntries(): any;
}

export interface ICmmnPalette {
  registerProvider(provider: ICmmnPaletteProvider): any;
}

export interface ICmmnPropertiesProvider {
  getTabs(elemnt): any;
}
