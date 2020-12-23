import AppBar from '@material-ui/core/AppBar';
import Hidden from '@material-ui/core/Hidden';
import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';

import clsx from 'clsx';
import React from 'react';
import { useSelector } from 'react-redux';
import { useTheme } from '@material-ui/core/styles';
import FullScreenToggle from '../../shared-components/FullScreenToggle';
import LanguageSwitcher from '../../shared-components/LanguageSwitcher';
import NavbarMobileToggleButton from '../../shared-components/NavbarMobileToggleButton';
import UserMenu from '../../shared-components/UserMenu';
import Logo from '../../shared-components/Logo';
import BpwSearch from '../../../elements/BpwSearch';
const useStyles = makeStyles((theme) => ({
  root: {},
}));

function ToolbarHorizontal(props) {
  const layout = useSelector(({ bpw }) => bpw.settings.current.layout);
  const toolbarTheme = useTheme(); //useSelector(selectToolbarTheme);

  const classes = useStyles(props);

  return (
    <AppBar
      id="bpw-toolbar"
      className={clsx(classes.root, 'flex relative z-10')}
      color="default"
      style={{ backgroundColor: toolbarTheme.palette.background.paper }}
      elevation={2}
    >
      <Toolbar className="container p-0 lg:px-24 min-h-48 md:min-h-64">
        {layout.navbar.display && (
          <Hidden lgUp>
            <NavbarMobileToggleButton className="w-40 h-40 p-0 mx-0 sm:mx-8" />
          </Hidden>
        )}
        <div className="flex flex-1">
          <Hidden mdDown>
            <Logo />
          </Hidden>
        </div>
        <div className="flex items-center px-8">
          <LanguageSwitcher />
          <FullScreenToggle />
          <BpwSearch />
          <UserMenu />
        </div>

      </Toolbar>
    </AppBar>
  );
}

export default React.memo(ToolbarHorizontal);
