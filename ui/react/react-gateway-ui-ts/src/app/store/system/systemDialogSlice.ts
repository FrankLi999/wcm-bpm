import { createSlice, createEntityAdapter } from '@reduxjs/toolkit';
const systemAdapter = createEntityAdapter({});

const systemSlice = createSlice({
  name: 'system/dialog',
  initialState: systemAdapter.getInitialState({
    systemDialog: {
      type: 'new',
      dialogType: '',
      props: {
        open: false,
      },
      data: null,
      rowIndex: null,
    },
  }),
  reducers: {
    openNewSystemDialog: (state, action) => {
      state.systemDialog = {
        type: 'new',
        dialogType: action.payload.dialogType,
        props: {
          open: true,
        },
        data: action.payload.data,
      };
    },
    closeNewSystemDialog: (state, action) => {
      state.systemDialog = {
        type: 'new',
        dialogType: action.payload.dialogType,
        props: {
          open: false,
        },
        data: null,
      };
    },
    openEditSystemDialog: (state, action) => {
      state.systemDialog = {
        type: 'edit',
        dialogType: action.payload.dialogType,
        props: {
          open: true,
        },
        data: action.payload.data,
        rowIndex: action.payload.rowIndex,
      };
    },
    closeEditSystemDialog: (state, action) => {
      state.systemDialog = {
        type: 'edit',
        dialogType: action.payload.dialogType,
        props: {
          open: false,
        },
        data: null,
      };
    },
  },
  extraReducers: {},
});

export const { openNewSystemDialog, closeNewSystemDialog, openEditSystemDialog, closeEditSystemDialog } = systemSlice.actions;

export default systemSlice.reducer;
