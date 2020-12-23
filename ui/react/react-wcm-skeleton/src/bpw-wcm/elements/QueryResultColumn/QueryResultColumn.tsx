import React, { useContext } from 'react';
import JsxParser from 'react-jsx-parser';
import AppContext from 'bpw-common/AppContext';
function QueryResultColumn(props) {
  const { rowValue, column } = props;
  const { wcmElements } = useContext(AppContext);
  function fragment() {
    return rowValue ? rowValue[column].value : '';
  }
  return <JsxParser components={wcmElements} jsx={fragment()} />;
}

export default QueryResultColumn;
