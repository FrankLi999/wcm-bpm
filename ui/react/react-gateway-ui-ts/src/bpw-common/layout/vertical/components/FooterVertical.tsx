import Typography from '@material-ui/core/Typography';
import React from 'react';
import { useSelector } from 'react-redux';
import { selectMainTheme } from '../../../store/settingsSlice';

function FooterVertical(props) {
  const theme = useSelector(selectMainTheme);
  return (
    <div id="bpw-footer" className="relative z-10" color="default" style={{ backgroundColor: theme.palette.background.paper }} elevation={2}>
      <Typography>Footer</Typography>
    </div>
  );
}

export default React.memo(FooterVertical);
