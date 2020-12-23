import React, { useContext } from 'react';
import JsxParser from 'react-jsx-parser';
import AppContext from 'bpw-common/AppContext';
function RenderProperty(props) {
  const { property, content } = props;
  const { wcmElements } = useContext(AppContext);
  function fragment() {
    content.properties[property] || content.elements[property] || '';
  }
  return <JsxParser components={wcmElements} jsx={fragment()} />;
}
export default RenderProperty;
