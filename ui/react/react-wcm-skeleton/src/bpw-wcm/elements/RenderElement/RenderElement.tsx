import React, { useContext } from 'react';
import JsxParser from 'react-jsx-parser';
import AppContext from 'bpw-common/AppContext';
function RenderElement(props) {
  const { element, content } = props;
  const { wcmElements } = useContext(AppContext);
  function fragment() {
    content.elements[element] || content.properties[element] || '';
  }
  return <JsxParser components={wcmElements} jsx={fragment()} />;
}

export default RenderElement;
