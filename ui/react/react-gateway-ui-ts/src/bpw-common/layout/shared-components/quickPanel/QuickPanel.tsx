import Drawer from '@material-ui/core/Drawer';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import withReducer from '../../../store/withReducer';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { toggleQuickPanel } from './store/stateSlice';
import reducer from './store';
import BpwScrollbars from '../../../elements/BpwScrollbars';

const useStyles = makeStyles((theme) => ({
  root: {
    width: 280,
  },
}));

function QuickPanel(props) {
  const dispatch = useDispatch();
  const state = useSelector(({ quickPanel }) => quickPanel.state);

  const classes = useStyles();

  return (
    <Drawer classes={{ paper: classes.root }} open={state} anchor="right" onClose={(ev) => dispatch(toggleQuickPanel())}>
      <BpwScrollbars>
        <Typography>Quick Panel</Typography>
      </BpwScrollbars>
    </Drawer>
  );
}

export default withReducer('quickPanel', reducer)(React.memo(QuickPanel));
