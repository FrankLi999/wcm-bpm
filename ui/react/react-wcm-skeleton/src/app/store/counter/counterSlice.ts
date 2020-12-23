import { createSlice, createEntityAdapter } from '@reduxjs/toolkit';
const counterAdapter = createEntityAdapter({});
const counterSlice = createSlice({
  name: 'counter',
  initialState: counterAdapter.getInitialState({ count: 0 }),
  reducers: {
    increment: (state, action) => {
      state.count = state.count + 1;
    },
    decrement: (state, action) => {
      state.count = state.count - 1;
    },
  }
})
export const { increment, decrement } = counterSlice.actions;
export default counterSlice.reducer;