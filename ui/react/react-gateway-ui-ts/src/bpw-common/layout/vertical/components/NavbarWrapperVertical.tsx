import Drawer from '@material-ui/core/Drawer';
import Hidden from '@material-ui/core/Hidden';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import clsx from 'clsx';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { navbarCloseFolded, navbarOpenFolded, navbarCloseMobile } from '../../../store/navbarSlice';

import NavbarMobileToggleFab from '../../shared-components/NavbarMobileToggleFab';
import NavbarLayout1 from './NavbarVertical';

const navbarWidth = 280;

const useStyles = makeStyles((theme) => ({
  wrapper: {
    display: 'flex',
    flexDirection: 'column',
    zIndex: 4,
    [theme.breakpoints.up('lg')]: {
      width: navbarWidth,
      minWidth: navbarWidth,
    },
  },
  wrapperFolded: {
    [theme.breakpoints.up('lg')]: {
      width: 64,
      minWidth: 64,
    },
  },
  navbar: {
    display: 'flex',
    overflow: 'hidden',
    flexDirection: 'column',
    flex: '1 1 auto',
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
  left: {
    left: 0,
  },
  right: {
    right: 0,
  },
  folded: {
    position: 'absolute',
    width: 64,
    minWidth: 64,
    top: 0,
    bottom: 0,
  },
  foldedAndOpened: {
    width: navbarWidth,
    minWidth: navbarWidth,
  },
  navbarContent: {
    flex: '1 1 auto',
  },
  foldedAndClosed: {
    '& $navbarContent': {
      '& .logo-icon': {
        width: 32,
        height: 32,
      },
      '& .logo-text': {
        opacity: 0,
      },
      '& .react-badge': {
        opacity: 0,
      },
      '& .list-item-text, & .arrow-icon, & .item-badge': {
        opacity: 0,
      },
      '& .list-subheader .list-subheader-text': {
        opacity: 0,
      },
      '& .list-subheader:before': {
        content: '""',
        display: 'block',
        position: 'absolute',
        minWidth: 16,
        borderTop: '2px solid',
        opacity: 0.2,
      },
      '& .collapse-children': {
        display: 'none',
      },
      '& .user': {
        '& .username, & .email': {
          opacity: 0,
        },
        '& .avatar': {
          width: 40,
          height: 40,
          top: 32,
          padding: 0,
        },
      },
      '& .list-item.active': {
        marginLeft: 12,
        width: 40,
        padding: 12,
        borderRadius: 20,
        '&.square': {
          borderRadius: 0,
          marginLeft: 0,
          paddingLeft: 24,
          width: '100%',
        },
      },
    },
  },
}));

function NavbarWrapperVertical(props) {
  const dispatch = useDispatch();
  const layout = useSelector(({ bpw }) => bpw.settings.current.layout);
  const navbarTheme = useTheme();
  const navbar = useSelector(({ bpw }) => bpw.navbar);

  const classes = useStyles();

  const { folded } = layout.navbar;
  const foldedAndClosed = folded && !navbar.foldedOpen;
  const foldedAndOpened = folded && navbar.foldedOpen;

  return (
    <>
      <div id="bpw-navbar" className={clsx(classes.wrapper, folded && classes.wrapperFolded)}>
        <Hidden mdDown>
          <div
            className={clsx(
              classes.navbar,
              classes[layout.navbar.position],
              folded && classes.folded,
              foldedAndOpened && classes.foldedAndOpened,
              foldedAndClosed && classes.foldedAndClosed
            )}
            onMouseEnter={() => foldedAndClosed && dispatch(navbarOpenFolded())}
            onMouseLeave={() => foldedAndOpened && dispatch(navbarCloseFolded())}
            style={{ backgroundColor: navbarTheme.palette.background.default }}
          >
            <NavbarLayout1 className={classes.navbarContent} />
          </div>
        </Hidden>

        <Hidden lgUp>
          <Drawer
            anchor={layout.navbar.position}
            variant="temporary"
            open={navbar.mobileOpen}
            classes={{
              paper: classes.navbar,
            }}
            onClose={() => dispatch(navbarCloseMobile())}
            ModalProps={{
              keepMounted: true, // Better open performance on mobile.
            }}
          >
            <NavbarLayout1 className={classes.navbarContent} />
          </Drawer>
        </Hidden>
      </div>

      {layout.navbar.display && !layout.toolbar.display && (
        <Hidden lgUp>
          <NavbarMobileToggleFab />
        </Hidden>
      )}
    </>
  );
}

export default React.memo(NavbarWrapperVertical);
