import Drawer from '@material-ui/core/Drawer';
import Hidden from '@material-ui/core/Hidden';
import clsx from 'clsx';
import React, { useImperativeHandle, useState } from 'react';
import BpwPageSimpleSidebarContent from './BpwPageSimpleSidebarContent';

function BpwPageSimpleSidebar(props, ref) {
  const [isOpen, setIsOpen] = useState(false);
  const { classes } = props;

  useImperativeHandle(ref, () => ({
    toggleSidebar: handleToggleDrawer,
  }));

  const handleToggleDrawer = () => {
    setIsOpen(!isOpen);
  };

  return (
    <>
      <Hidden lgUp={props.variant === 'permanent'}>
        <Drawer
          variant="temporary"
          anchor={props.position}
          open={isOpen}
          onClose={(ev) => handleToggleDrawer()}
          classes={{
            root: clsx(classes.sidebarWrapper, props.variant),
            paper: clsx(classes.sidebar, props.variant, props.position === 'left' ? classes.leftSidebar : classes.rightSidebar),
          }}
          ModalProps={{
            keepMounted: true, // Better open performance on mobile.
          }}
          container={props.rootRef.current}
          BackdropProps={{
            classes: {
              root: classes.backdrop,
            },
          }}
          style={{ position: 'absolute' }}
        >
          <BpwPageSimpleSidebarContent {...props} />
        </Drawer>
      </Hidden>
      {props.variant === 'permanent' && (
        <Hidden mdDown>
          <Drawer
            variant="permanent"
            className={clsx(classes.sidebarWrapper, props.variant)}
            open={isOpen}
            classes={{
              paper: clsx(classes.sidebar, props.variant, props.position === 'left' ? classes.leftSidebar : classes.rightSidebar),
            }}
          >
            <BpwPageSimpleSidebarContent {...props} />
          </Drawer>
        </Hidden>
      )}
    </>
  );
}

export default React.forwardRef(BpwPageSimpleSidebar);
