import { ThemeProvider } from '@material-ui/core/styles';
import React, { useEffect, useLayoutEffect } from 'react';
import { useSelector } from 'react-redux';
import { selectMainTheme } from '../../store/settingsSlice';

const useEnhancedEffect = typeof window === 'undefined' ? useEffect : useLayoutEffect;

function BpwTheme(props) {
  const direction = useSelector(({ bpw }) => bpw.settings.defaults.direction);
  const mainTheme = useSelector(selectMainTheme);

  useEnhancedEffect(() => {
    document.body.dir = direction;
  }, [direction]);

  // console.warn('BpwTheme:: rendered',mainTheme);
  return <ThemeProvider theme={mainTheme}>{props.children}</ThemeProvider>;
}

export default React.memo(BpwTheme);
