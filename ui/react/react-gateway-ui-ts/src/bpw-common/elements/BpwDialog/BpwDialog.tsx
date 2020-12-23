import Dialog from '@material-ui/core/Dialog';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { closeDialog } from '../../store/dialogSlice';

function BpwDialog(props) {
  const dispatch = useDispatch();
  const state = useSelector(({ bpw }) => bpw.dialog.state);
  const options = useSelector(({ bpw }) => bpw.dialog.options);

  return (
    <Dialog
      open={state}
      onClose={(ev) => dispatch(closeDialog())}
      aria-labelledby="bpw-dialog-title"
      classes={{
        paper: 'rounded-8',
      }}
      {...options}
    />
  );
}

export default BpwDialog;
