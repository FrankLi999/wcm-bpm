import clsx from 'clsx';
import React from 'react';
import { useSelector } from 'react-redux';
import { selectNavigation } from '../../store/navigationSlice';
import BpwNavigation from '../../elements/BpwNavigation';

function Navigation(props) {
  const navigation = useSelector(selectNavigation);
  return (
    <BpwNavigation className={clsx('navigation', props.className)} navigation={navigation} layout={props.layout} dense={props.dense} active={props.active} />
  );
}

Navigation.defaultProps = {
  layout: 'vertical',
};

export default React.memo(Navigation);
