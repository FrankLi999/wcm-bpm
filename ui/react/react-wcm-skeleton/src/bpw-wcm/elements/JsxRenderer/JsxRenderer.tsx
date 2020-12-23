import React, { useContext } from 'react';
import JsxParser from 'react-jsx-parser';
import AppContext from 'bpw-common/AppContext';
function JsxRenderer(props) {
  const { wcmElements } = useContext(AppContext);
  return <JsxParser components={wcmElements} jsx={props.jsx} />;
}
export default JsxRenderer;
