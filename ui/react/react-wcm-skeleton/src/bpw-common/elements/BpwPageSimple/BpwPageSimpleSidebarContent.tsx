import React from 'react';
import { useSelector } from 'react-redux';
import BpwScrollbars from '../BpwScrollbars';
import { useTheme, ThemeProvider } from '@material-ui/core/styles';
import { selectContrastMainTheme } from '../../store/settingsSlice';
import clsx from 'clsx';

function BpwPageSimpleSidebarContent(props) {
  const theme = useTheme();
  const contrastTheme = useSelector(selectContrastMainTheme(theme.palette.primary.main));

  const { classes } = props;

  return (
    <BpwScrollbars enable={props.innerScroll}>
      {props.header && (
        <ThemeProvider theme={contrastTheme}>
          <div className={clsx(classes.sidebarHeader, props.variant, props.sidebarInner && classes.sidebarHeaderInnerSidebar)}>{props.header}</div>
        </ThemeProvider>
      )}

      {props.content && <div className={classes.sidebarContent}>{props.content}</div>}
    </BpwScrollbars>
  );
}

export default BpwPageSimpleSidebarContent;
