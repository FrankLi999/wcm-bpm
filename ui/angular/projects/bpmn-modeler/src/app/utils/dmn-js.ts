import * as _DmnModeler from "dmn-js/dist/dmn-modeler.production.min.js";
// import * as _DmnModeler from "dmn-js/lib/Modeler";
import _DmnViewer from "dmn-js/dist/dmn-modeler.production.min.js";
import * as _DmnNavigatedViewer from "dmn-js/dist/dmn-navigated-viewer.production.min.js";
import * as _dmnPropertiesPanelModule from "dmn-js-properties-panel";
// import * as _dmnPropertiesProvider from "dmn-js-properties-panel/lib/provider/dmn";
import * as _dmnDrdAdapterModule from "dmn-js-properties-panel/lib/adapter/drd";

import _dmnPropertiesProviderModule from "dmn-js-properties-panel/lib/provider/camunda";

import * as _dmnEntryFactory from "dmn-js-properties-panel/lib/factory/EntryFactory";
// import _dmnPaletteProvider from "dmn-js/lib/features/palette/PaletteProvider";
import _camundaDmnModdleDescriptor from "camunda-dmn-moddle/resources/camunda.json";
// export const DmnInjectionNames = {
//   eventBus: "eventBus",
//   dmnFactory: "dmnFactory",
//   elementRegistry: "elementRegistry",
//   translate: "translate",
//   propertiesProvider: "propertiesProvider",
//   dmnPropertiesProvider: "dmnPropertiesProvider",
//   paletteProvider: "paletteProvider",
//   originalPaletteProvider: "originalPaletteProvider",
// };

export const DmnModeler = _DmnModeler;
export const DmnViewer = _DmnViewer;
export const DmnNavigatedViewer = _DmnNavigatedViewer;
export const dmnPropertiesPanelModule = _dmnPropertiesPanelModule;
export const dmnPropertiesProviderModule = _dmnPropertiesProviderModule;
export const dmnDrdAdapterModule = _dmnDrdAdapterModule;
// export const dmnEntryFactory = _dmnEntryFactory;
// export const OriginalDmnPaletteProvider = _dmnPaletteProvider;
// export const OriginalDmnPropertiesProvider = _dmnPropertiesProvider;
export const camundaDmnModdleDescriptor = _camundaDmnModdleDescriptor;

export interface IDmnPaletteProvider {
  getPaletteEntries(): any;
}

export interface IDmnPalette {
  registerProvider(provider: IDmnPaletteProvider): any;
}

export interface IDmnPropertiesProvider {
  getTabs(elemnt): any;
}
