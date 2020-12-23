import {delay, filter, map} from 'rxjs/operators';
import {decrement} from './counterSlice';

const counterEpic = action$ => action$.pipe(
  filter(action => 'counter/increment'=== action.type),
  delay(1000),
  map((action) => {
    return decrement();
  })
);

export default counterEpic;
