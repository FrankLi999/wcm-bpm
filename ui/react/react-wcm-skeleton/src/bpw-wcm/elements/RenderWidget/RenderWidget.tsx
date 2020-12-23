import React, { useContext } from 'react';
import AppContext from 'bpw-common/AppContext';
function RenderWidget(props) {
  const { element, data } = props;
  const { dynamicElements } = useContext(AppContext);
  const element = dynamicElements[element];
  return <element data />; // todo: pass props
}
export default RenderWidget;
