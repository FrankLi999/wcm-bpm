import BpwLoading from '../BpwLoading';
import PropTypes from 'prop-types';
import React from 'react';

/**
 * React Suspense defaults
 * For to Avoid Repetition
 */ function BpwSuspense(props) {
  return <React.Suspense fallback={<BpwLoading {...props.loadingProps} />}>{props.children}</React.Suspense>;
}

BpwSuspense.propTypes = {
  loadingProps: PropTypes.object,
};

BpwSuspense.defaultProps = {
  loadingProps: {
    delay: 0,
  },
};

export default BpwSuspense;
