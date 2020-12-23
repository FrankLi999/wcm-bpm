import Drawer from '@material-ui/core/Drawer';
import Hidden from '@material-ui/core/Hidden';
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';
import NavbarMobileToggleFab from '../../shared-components/NavbarMobileToggleFab';
import clsx from 'clsx';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { navbarCloseMobile } from '../../../store/navbarSlice';
import NavbarHorizontal from './NavbarHorizontal';
import NavbarMobileHorizontal from './NavbarMobileHorizontal';

const navbarWidth = 280;

const useStyles = makeStyles((theme) => ({
  navbar: {
    display: 'flex',
    overflow: 'hidden',
    height: 64,
    minHeight: 64,
    alignItems: 'center',
    boxShadow: theme.shadows[3],
    zIndex: 6,
  },
  navbarMobile: {
    display: 'flex',
    overflow: 'hidden',
    flexDirection: 'column',
    width: navbarWidth,
    minWidth: navbarWidth,
    height: '100%',
    zIndex: 4,
    transition: theme.transitions.create(['width', 'min-width'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.shorter,
    }),
    boxShadow: theme.shadows[3],
  },
}));

function NavbarWrapperHorizontal(props) {
  const dispatch = useDispatch();
  const layout = useSelector(({ bpw }) => bpw.settings.current.layout);
  const navbar = useSelector(({ bpw }) => bpw.navbar);

  const classes = useStyles(props);

  return (
    <>
      <Hidden mdDown>
        <Paper className={clsx(classes.navbar)} square elevation={2}>
          <NavbarHorizontal />
        </Paper>
      </Hidden>

      <Hidden lgUp>
        <Drawer
          anchor="left"
          variant="temporary"
          open={navbar.mobileOpen}
          classes={{
            paper: classes.navbarMobile,
          }}
          onClose={(ev) => dispatch(navbarCloseMobile())}
          ModalProps={{
            keepMounted: true, // Better open performance on mobile.
          }}
        >
          <NavbarMobileHorizontal />
        </Drawer>
      </Hidden>

      {layout.navbar.display && !layout.toolbar.display && (
        <Hidden lgUp>
          <NavbarMobileToggleFab />
        </Hidden>
      )}
    </>
  );
}

export default React.memo(NavbarWrapperHorizontal);
