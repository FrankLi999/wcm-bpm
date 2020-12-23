import React from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from '@reduxjs/toolkit';
import { setWcmSystem } from '../../store/wcmSystemSlice';
function WcmSystem(props) {
  return <>{props.children}</>;
}
function mapDispatchToProps(dispatch) {
  return bindActionCreators(
    {
      setWcmSystem,
    },
    dispatch
  );
}

export default connect(null, mapDispatchToProps)(WcmSystem);
