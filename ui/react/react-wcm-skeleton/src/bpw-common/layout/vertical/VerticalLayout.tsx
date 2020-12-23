import { makeStyles } from '@material-ui/core/styles';
import clsx from 'clsx';
import React, { useContext } from 'react';
import { useSelector } from 'react-redux';
import { renderRoutes } from 'react-router-config';
import FooterVertical from './components/FooterVertical';
import LeftSideVertical from './components/LeftSideVertical';
import NavbarWrapperVertical from './components/NavbarWrapperVertical';
import RightSideVertical from './components/RightSideVertical';
import ToolbarVertical from './components/ToolbarVertical';
import AppContext from '../../AppContext';
import BpwDialog from '../../elements/BpwDialog';
import BpwMessage from '../../elements/BpwMessage';
import BpwScrollbars from '../../elements/BpwScrollbars';
import BpwSuspense from '../../elements/BpwSuspense';
const useStyles = makeStyles((theme) => ({
  root: {
    position: 'relative',
    display: 'flex',
    flexDirection: 'row',
    width: '100%',
    height: '100%',
    overflow: 'hidden',
    backgroundColor: theme.palette.background.default,
    color: theme.palette.text.primary,
    '&.boxed': {
      maxWidth: 1280,
      margin: '0 auto',
      boxShadow: theme.shadows[3],
    },
    '&.scroll-body': {
      '& $wrapper': {
        height: 'auto',
        flex: '0 0 auto',
        overflow: 'auto',
      },
      '& $contentWrapper': {},
      '& $content': {},
    },
    '&.scroll-content': {
      '& $wrapper': {},
      '& $contentWrapper': {},
      '& $content': {},
    },
    '& .navigation': {
      '& .list-subheader-text, & .list-item-text, & .item-badge, & .arrow-icon': {
        transition: theme.transitions.create('opacity', {
          duration: theme.transitions.duration.shortest,
          easing: theme.transitions.easing.easeInOut,
        }),
      },
    },
  },
  wrapper: {
    display: 'flex',
    position: 'relative',
    width: '100%',
    height: '100%',
    flex: '1 1 auto',
  },
  contentWrapper: {
    display: 'flex',
    flexDirection: 'column',
    position: 'relative',
    zIndex: 3,
    overflow: 'hidden',
    flex: '1 1 auto',
  },
  content: {
    position: 'relative',
    display: 'flex',
    overflow: 'auto',
    flex: '1 1 auto',
    flexDirection: 'column',
    width: '100%',
    '-webkit-overflow-scrolling': 'touch',
    zIndex: 2,
  },
}));

function VerticalLayout(props) {
  const layout = useSelector(({ bpw }) => bpw.settings.current.layout);

  const appContext = useContext(AppContext);
  const classes = useStyles(props);
  const { routes } = appContext;

  // console.warn('BpwLayout:: rendered');

  switch (layout.scroll) {
    case 'body': {
      return (
        <div id="bpw-layout" className={clsx(classes.root, layout.mode, `scroll-${layout.scroll}`)}>
          {layout.leftSidePanel.display && <LeftSideVertical />}

          <div className="flex flex-1 flex-col overflow-hidden relative">
            {layout.toolbar.display && layout.toolbar.style === 'fixed' && layout.toolbar.position === 'above' && <ToolbarVertical />}

            <BpwScrollbars className="overflow-auto" scrollToTopOnRouteChange>
              {layout.toolbar.display && layout.toolbar.style !== 'fixed' && layout.toolbar.position === 'above' && <ToolbarVertical />}

              <div className={classes.wrapper}>
                {layout.navbar.display && layout.navbar.position === 'left' && <NavbarWrapperVertical />}

                <div className={classes.contentWrapper}>
                  {layout.toolbar.display && layout.toolbar.position === 'below' && <ToolbarVertical />}

                  <div className={classes.content}>
                    <BpwDialog />

                    <BpwSuspense>{renderRoutes(routes)}</BpwSuspense>

                    {props.children}
                  </div>

                  {layout.footer.display && layout.footer.position === 'below' && <FooterVertical />}
                </div>

                {layout.navbar.display && layout.navbar.position === 'right' && <NavbarWrapperVertical />}
              </div>

              {layout.footer.display && layout.footer.style !== 'fixed' && layout.footer.position === 'above' && <FooterVertical />}
            </BpwScrollbars>

            {layout.footer.display && layout.footer.style === 'fixed' && layout.footer.position === 'above' && <FooterVertical />}
          </div>

          {layout.rightSidePanel.display && <RightSideVertical />}

          <BpwMessage />
        </div>
      );
    }
    case 'content':
    default: {
      return (
        <div id="bpw-layout" className={clsx(classes.root, layout.mode, `scroll-${layout.scroll}`)}>
          {layout.leftSidePanel.display && <LeftSideVertical />}

          <div className="flex flex-1 flex-col overflow-hidden relative">
            {layout.toolbar.display && layout.toolbar.position === 'above' && <ToolbarVertical />}

            <div className={classes.wrapper}>
              {layout.navbar.display && layout.navbar.position === 'left' && <NavbarWrapperVertical />}

              <div className={classes.contentWrapper}>
                {layout.toolbar.display && layout.toolbar.position === 'below' && layout.toolbar.style === 'fixed' && <ToolbarVertical />}

                <BpwScrollbars className={classes.content} scrollToTopOnRouteChange>
                  {layout.toolbar.display && layout.toolbar.position === 'below' && layout.toolbar.style !== 'fixed' && <ToolbarVertical />}

                  <BpwDialog />

                  <BpwSuspense>{renderRoutes(routes)}</BpwSuspense>

                  {props.children}

                  {layout.footer.display && layout.footer.position === 'below' && layout.footer.style !== 'fixed' && <FooterVertical />}
                </BpwScrollbars>

                {layout.footer.display && layout.footer.position === 'below' && layout.footer.style === 'fixed' && <FooterVertical />}
              </div>

              {layout.navbar.display && layout.navbar.position === 'right' && <NavbarWrapperVertical />}
            </div>

            {layout.footer.display && layout.footer.position === 'above' && <FooterVertical />}
          </div>

          {layout.rightSidePanel.display && <RightSideVertical />}

          <BpwMessage />
        </div>
      );
    }
  }
}

export default React.memo(VerticalLayout);
