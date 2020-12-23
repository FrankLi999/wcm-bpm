import { createSlice, createEntityAdapter } from '@reduxjs/toolkit';
import axios from 'axios';
import { filter, map} from 'rxjs/operators';
import { from } from 'rxjs';
export const loadEnum = () => async (dispatch) => {
  http: return from(axios.get('http://localhost:28082/gateway-admin/api/platform/enum'))
    .pipe(
      map((resp) => resp.data),
      filter((data) => !!data)
    )
    .subscribe(
      (systemEnum) => {
        return dispatch(setEnum(systemEnum));
      },
      (error) => {
        return dispatch(enumError(error));
      }
    );
};

const enumAdapter = createEntityAdapter({});
const enumSlice = createSlice({
  name: 'system/enum',
  initialState: enumAdapter.getInitialState({ systemEnum: {}, error: null }),
  reducers: {
    setEnum: (state, action) => {
      state.systemEnum = {
        ...action.payload,
      };
      state.error = null;
    },
    enumError: (state, action) => {
      state.systemEnum = {};
      state.error = action.payload;
    },
    clearEnum: (state, action) => {
      state.systemEnum = {};
      state.error = null;
    },
  },
  extraReducers: {},
});
export const { setEnum, enumError, clearEnum } = enumSlice.actions;

export default enumSlice.reducer;
