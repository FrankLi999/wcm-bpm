import { makeStyles } from '@material-ui/core/styles';
import clsx from 'clsx';
import React from 'react';
import { useSelector } from 'react-redux';
import { renderRoutes } from 'react-router-config';
import FooterHorizontal from './components/FooterHorizontal';
import LeftSideHorizontal from './components/LeftSideHorizontal';
import NavbarWrapperHorizontal from './components/NavbarWrapperHorizontal';
import RightSideHorizontal from './components/RightSideHorizontal';
import ToolbarHorizontal from './components/ToolbarHorizontal';
import BpwDialog from '../../elements/BpwDialog';
import BpwMessage from '../../elements/BpwMessage';
import BpwScrollbars from '../../elements/BpwScrollbars';
import BpwSuspense from '../../elements/BpwSuspense';
import AppContext from '../../AppContext';
const useStyles = makeStyles((theme) => ({
  root: {
    position: 'relative',
    display: 'flex',
    flexDirection: 'row',
    width: '100%',
    height: '100%',
    overflow: 'hidden',
    '&.boxed': {
      maxWidth: 1120,
      margin: '0 auto',
      boxShadow: theme.shadows[3],
    },
    '&.container': {
      '& .container': {
        maxWidth: 1120,
        width: '100%',
        margin: '0 auto',
      },
      '& .navigation': {},
    },
  },
  content: {
    display: 'flex',
    overflow: 'auto',
    flex: '1 1 auto',
    flexDirection: 'column',
    width: '100%',
    '-webkit-overflow-scrolling': 'touch',
    zIndex: 4,
  },
  toolbarWrapper: {
    display: 'flex',
    position: 'relative',
    zIndex: 5,
  },
  toolbar: {
    display: 'flex',
    flex: '1 0 auto',
  },
  footerWrapper: {
    position: 'relative',
    zIndex: 5,
  },
  footer: {
    display: 'flex',
    flex: '1 0 auto',
  },
}));

function HorizontalLayout(props) {
  const layout = useSelector(({ bpw }) => bpw.settings.current.layout);

  const classes = useStyles(props);

  return (
    <AppContext.Consumer>
      {({ routes }) => (
        <div id="bpw-layout" className={clsx(classes.root, layout.mode)}>
          {layout.leftSidePanel.display && <LeftSideHorizontal />}

          <div className="flex flex-1 flex-col overflow-hidden relative">
            {layout.toolbar.display && layout.toolbar.position === 'above' && <ToolbarHorizontal />}

            {layout.navbar.display && <NavbarWrapperHorizontal />}

            {layout.toolbar.display && layout.toolbar.position === 'below' && <ToolbarHorizontal />}

            <BpwScrollbars className={clsx(classes.content)} scrollToTopOnRouteChange>
              <BpwDialog />

              <div className="flex flex-auto flex-col relative h-full">
                <BpwSuspense>{renderRoutes(routes)}</BpwSuspense>

                {props.children}

                {layout.footer.display && layout.footer.style === 'static' && <FooterHorizontal />}
              </div>
            </BpwScrollbars>

            {layout.footer.display && layout.footer.style === 'fixed' && <FooterHorizontal />}
          </div>

          {layout.rightSidePanel.display && <RightSideHorizontal />}

          <BpwMessage />
        </div>
      )}
    </AppContext.Consumer>
  );
}

export default React.memo(HorizontalLayout);
