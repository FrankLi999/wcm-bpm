import Typography from '@material-ui/core/Typography';
import React from 'react';
import { useTheme } from '@material-ui/core/styles';

function FooterHorizontal(props) {
  const theme = useTheme();

  return (
    <div id="bpw-footer" className="relative z-10" color="default" style={{ backgroundColor: theme.palette.background.paper }} elevation={2}>
      <Typography>Footer</Typography>
    </div>
  );
}

export default React.memo(FooterHorizontal);
