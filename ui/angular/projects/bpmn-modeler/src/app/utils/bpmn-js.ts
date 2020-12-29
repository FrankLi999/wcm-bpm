// import _Modeler from 'bpmn-js/lib/Modeler.js';
import * as _BpmnModeler from "bpmn-js/dist/bpmn-modeler.production.min.js";
import * as _BpmnViewer from "bpmn-js/dist/bpmn-viewer.production.min.js";
import * as _BpmnNavigatedViewer from "bpmn-js/dist/bpmn-navigated-viewer.production.min.js";
import * as _bpmnPropertiesPanelModule from "bpmn-js-properties-panel";
import * as _bpmnPropertiesProviderModule from "bpmn-js-properties-panel/lib/provider/camunda";
import * as _BpmnPropertiesProvider from "bpmn-js-properties-panel/lib/provider/bpmn";
import * as _bpmnEntryFactory from "bpmn-js-properties-panel/lib/factory/EntryFactory";
import _bpmnPaletteProvider from "bpmn-js/lib/features/palette/PaletteProvider";
import * as _camundaBpmnModdleDescriptor from "camunda-bpmn-moddle/resources/camunda.json";
// import * as _colorRenderer from "bpmn-js-in-color/colors/ColorRenderer";
// import * as _colorContextPadProvider from "bpmn-js-in-color/colors/ColorContextPadProvider";
// import * as _colorPopupProvider from "bpmn-js-in-color/colors/ColorPopupProvider";
export const BpmnInjectionNames = {
  eventBus: "eventBus",
  bpmnFactory: "bpmnFactory",
  elementRegistry: "elementRegistry",
  translate: "translate",
  propertiesProvider: "propertiesProvider",
  bpmnPropertiesProvider: "bpmnPropertiesProvider",
  paletteProvider: "paletteProvider",
  originalPaletteProvider: "originalPaletteProvider",
};

export const BpmnModeler = _BpmnModeler;
export const BpmnViewer = _BpmnViewer;
export const BpmnNavigatedViewer = _BpmnNavigatedViewer;
export const bpmnPropertiesPanelModule = _bpmnPropertiesPanelModule;
export const bpmnPropertiesProviderModule = _bpmnPropertiesProviderModule;
export const bpmnEntryFactory = _bpmnEntryFactory;
export const OriginalBpmnPaletteProvider = _bpmnPaletteProvider;
export const OriginalBpmnPropertiesProvider = _BpmnPropertiesProvider;
export const camundaBpmnModdleDescriptor = _camundaBpmnModdleDescriptor;
// export const colorRenderer = _colorRenderer;
// export const colorContextPadProvider = _colorContextPadProvider;
// export const colorPopupProvider = _colorPopupProvider;
export interface IBpmnPaletteProvider {
  getPaletteEntries(): any;
}

export interface IBpmnPalette {
  registerProvider(provider: IBpmnPaletteProvider): any;
}

export interface IBpmnPropertiesProvider {
  getTabs(elemnt): any;
}
