import { createSlice, createEntityAdapter } from '@reduxjs/toolkit';
const handlersAdapter = createEntityAdapter({});

const defaultSelectorFormState = {
  name: '',
  type: 'custom', //or full
  andOr: 'and', // or or
  conditions: [{ param: 'uri', equation: '=', value: '' }],
  continue: true,
  logging: true,
  active: true,
  serviceName: '',
  order: 0, //0 - 100
};

const defaultSelectorRuleFormState = {
  name: '',
  andOr: 'and', // or or
  conditions: [{ param: 'uri', equation: '=', value: '' }],
  logging: true,
  active: true,
  springCloudPath: '',
  springCloudTimeout: '',
  order: 0, //0 - 100
};
const handlersSlice = createSlice({
  name: 'handlers/dialog',
  initialState: handlersAdapter.getInitialState({
    handlerDialog: {
      type: 'new',
      selectorType: '',
      isSelector: true,
      props: {
        open: false,
      },
      data: {
        ...defaultSelectorFormState,
      },
      rowIndex: null,
    },
  }),
  reducers: {
    openNewSelectorDialog: (state, action) => {
      state.handlerDialog = {
        type: 'new',
        selectorType: action.payload.selectorType,
        isSelector: true,
        props: {
          open: true,
        },
        data: {
          ...defaultSelectorFormState,
        },
      };
    },
    closeNewSelectorDialog: (state, action) => {
      state.handlerDialog = {
        type: 'new',
        selectorType: action.payload.selectorType,
        isSelector: true,
        props: {
          open: false,
        },
        data: null,
      };
    },
    openEditSelectorDialog: (state, action) => {
      state.handlerDialog = {
        type: 'edit',
        selectorType: action.payload.selectorType,
        isSelector: true,
        props: {
          open: true,
        },
        data: action.payload.data,
        rowIndex: action.payload.rowIndex,
      };
    },
    closeEditSelectorDialog: (state, action) => {
      state.handlerDialog = {
        type: 'edit',
        selectorType: action.payload.selectorType,
        isSelector: true,
        props: {
          open: false,
        },
        data: null,
      };
    },
    openNewSelectorRuleDialog: (state, action) => {
      state.handlerDialog = {
        type: 'new',
        selectorType: action.payload.selectorType,
        selector: action.payload.selector,
        isSelector: false,
        props: {
          open: true,
        },
        data: {
          ...defaultSelectorRuleFormState,
        },
      };
    },
    closeNewSelectorRuleDialog: (state, action) => {
      state.handlerDialog = {
        type: 'new',
        selectorType: action.payload.selectorType,
        selector: action.payload.selector,
        isSelector: false,
        props: {
          open: false,
        },
        data: null,
      };
    },
    openEditSelectorRuleDialog: (state, action) => {
      state.handlerDialog = {
        type: 'edit',
        selectorType: action.payload.selectorType,
        selector: action.payload.selector,
        isSelector: false,
        props: {
          open: true,
        },
        data: action.payload.data,
        rowIndex: action.payload.rowIndex,
      };
    },
    closeEditSelectorRuleDialog: (state, action) => {
      state.handlerDialog = {
        type: 'edit',
        selectorType: action.payload.selectorType,
        selector: action.payload.selector,
        isSelector: false,
        props: {
          open: false,
        },
        data: null,
      };
    },
  },
  extraReducers: {},
});

export const {
  openNewSelectorDialog,
  closeNewSelectorDialog,
  openEditSelectorDialog,
  closeEditSelectorDialog,
  openNewSelectorRuleDialog,
  closeNewSelectorRuleDialog,
  openEditSelectorRuleDialog,
  closeEditSelectorRuleDialog,
} = handlersSlice.actions;

export default handlersSlice.reducer;
