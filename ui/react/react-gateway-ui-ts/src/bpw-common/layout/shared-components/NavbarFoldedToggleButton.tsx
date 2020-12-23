import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setDefaultSettings } from '../../store/settingsSlice';
import _ from '../../lodash';
function NavbarFoldedToggleButton(props) {
  const dispatch = useDispatch();
  const settings = useSelector(({ bpw }) => bpw.settings.current);

  return (
    <IconButton
      className={props.className}
      onClick={() => {
        dispatch(setDefaultSettings(_.set({}, 'layout.navbar.folded', !settings.layout.navbar.folded)));
      }}
      color="inherit"
    >
      {props.children}
    </IconButton>
  );
}

NavbarFoldedToggleButton.defaultProps = {
  children: <Icon>menu</Icon>,
};

export default NavbarFoldedToggleButton;
