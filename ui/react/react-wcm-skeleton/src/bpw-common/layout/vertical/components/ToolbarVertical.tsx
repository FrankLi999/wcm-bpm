import AppBar from '@material-ui/core/AppBar';
import Hidden from '@material-ui/core/Hidden';
import { makeStyles, ThemeProvider } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import clsx from 'clsx';
import React from 'react';
import { useSelector } from 'react-redux';
import { selectToolbarTheme } from '../../../store/settingsSlice';
import FullScreenToggle from '../../shared-components/FullScreenToggle';
import LanguageSwitcher from '../../shared-components/LanguageSwitcher';
import BpwSearch from '../../../elements/BpwSearch';
import BpwShortcuts from '../../../elements/BpwShortcuts';
import NavbarMobileToggleButton from '../../shared-components/NavbarMobileToggleButton';
import QuickPanelToggleButton from '../../shared-components/quickPanel/QuickPanelToggleButton';
const useStyles = makeStyles((theme) => ({
  root: {},
}));

function ToolbarVertical(props) {
  const layout = useSelector(({ bpw }) => bpw.settings.current.layout);
  const toolbarTheme = useSelector(selectToolbarTheme);

  const classes = useStyles(props);

  return (
    <ThemeProvider theme={toolbarTheme}>
      <AppBar
        id="bpw-toolbar"
        className={clsx(classes.root, 'flex relative z-10')}
        color="default"
        style={{ backgroundColor: toolbarTheme.palette.background.paper }}
        elevation={2}
      >
        <Toolbar className="p-0 min-h-48 md:min-h-64">
          {layout.navbar.display && layout.navbar.position === 'left' && (
            <Hidden lgUp>
              <NavbarMobileToggleButton className="w-40 h-40 p-0 mx-0 sm:mx-8" />
            </Hidden>
          )}

          <div className="flex flex-1">
            <Hidden mdDown>
              <BpwShortcuts className="px-16" />
            </Hidden>
          </div>

          <div className="flex items-center px-16">
            <LanguageSwitcher />

            <FullScreenToggle />

            <BpwSearch />

            <QuickPanelToggleButton />
          </div>

          {layout.navbar.display && layout.navbar.position === 'right' && (
            <Hidden lgUp>
              <NavbarMobileToggleButton />
            </Hidden>
          )}
        </Toolbar>
      </AppBar>
    </ThemeProvider>
  );
}

export default React.memo(ToolbarVertical);
