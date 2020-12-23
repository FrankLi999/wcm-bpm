import { createSlice } from '@reduxjs/toolkit';
const initialState = {};
const wcmSystemSlice = createSlice({
  name: 'wcm/wcmSystem',
  initialState,
  reducers: {
    setWcmSystem: (state, action) => action.payload,
    resetWcmSystem: (state, action) => initialState,
  },
  extraReducers: {},
});

export const { setWcmSystem, resetWcmSystem } = wcmSystemSlice.actions;
export default wcmSystemSlice.reducer;
