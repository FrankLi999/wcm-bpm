import React, { useContext } from 'react';
import JsxParser from 'react-jsx-parser';
import AppContext from 'bpw-common/AppContext';
function RendererFragment(props) {
  const { content, body } = props;
  const { wcmElements } = useContext(AppContext);
  function _resolveWidgetBody() {
    var regex = /\$\{.*?\}/g;
    let matches = body.match(regex);
    let result = body;
    for (let matche of matches) {
      let data = matche
        .substring(2, matche.length - 1)
        .trim()
        .split(':');
      if (data[0] === 'query') {
        result = result.replace(matche, content[data[1]]);
      } else if (data[0] === 'elements') {
        result = result.replace(matche, content.elements[data[1]]);
      } else if (data[0] === 'properties') {
        result = result.replace(matche, content.properties[data[1]]);
      }
    }
    return result;
  }
  return <JsxParser components={wcmElements} jsx={_resolveWidgetBody()} />;
}
export default RendererFragment;
