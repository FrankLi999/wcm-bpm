import React, { useContext, useEffect, useState } from 'react';
import { tap } from 'rxjs/operators';
import AppContext from 'bpw-common/AppContext';
import JsxParser from 'react-jsx-parser';
import queryStatementService from '../../services/QueryStatementService';
import WcmUtils from '../../utils/WcmUtils';
function QueryResultRenderer(props) {
  const { wcmElements } = useContext(AppContext);
  const { repository, workspace, siteAreaKey, rendererTemplate, renderer } = props;
  const library = WcmUtils.library(rendererTemplate.resourceName);
  const name = WcmUtils.itemName(rendererTemplate.resourceName);
  const [queryResult, setQueryResult] = useState(null);
  useEffect(() => {
    queryStatementService
      .executeQueryStatement({ repository, workspace, library, name })
      .pipe(tap((queryResult) => setQueryResult(queryResult)))
      .subscribe();
    //todo, clean it up
  }, [renderer]);

  function renderCode() {
    return rendererTemplate.code !== undefined;
  }

  function renderLayout() {
    return rendererTemplate.rows !== undefined;
  }

  function queryResultId() {
    return `${renderer}_${siteAreaKey}_${renderer}`;
  }

  function renderRow(queryResultId, rowValue) {
    let result = `${rendererTemplate.code}`.replace(
      '<query-result-column ',
      `<query-result-column queryresultid='${queryResultId}' rowValue={${JSON.stringify(rowValue)}} `
    );
    return this._resolveWidgetBody(result, rowValue);
  }

  function isQuery(source) {
    return 'query' === source;
  }

  function _resolveWidgetBody(content, rowValue) {
    var regex = /\$\{.*?\}/g;
    let matches = content.match(regex);
    let result = content;
    for (let matche of matches) {
      let data = matche
        .substring(2, matche.length - 1)
        .trim()
        .split(':');
      result = result.replace(matche, rowValue[data[1]].value);
    }
    return result;
  }

  function _columnValue(rowValue, element) {
    if (isQuery(element.source)) {
      return rowValue[element.name].value;
    } else {
      return _resolveWidgetBody(element.body, rowValue);
    }
  }

  return (
    <>
      {queryResult &&
        queryResult.rows.map((rowValue, rowValueIndex) => {
          return (
            <div key={rowValueIndex}>
              {renderCode() && <JsxParser components={wcmElements} jsx={renderRow(queryResultId(), rowValue)} />}
              {renderLayout &&
                rendererTemplate.rows.map((row, rowIndex) => (
                  <div key={rowIndex} className="card container" style={{ flexDirection: 'row', padding: '6px' }}>
                    {row.columns.map((column, columnIndex) => (
                      <div key={columnIndex} style={{ flexDirection: 'column', layoutAlign: 'start', flex: column.width ? column.width : 100 }}>
                        {column.elements.map((element, elementIndex) => (
                          <div key={elementIndex} style={{ flexDirection: 'row', layoutAlign: 'start', flex: 'column.width ? column.width : 100' }}>
                            {rendererTemplate.rows.map((rtRow, rtRowIndex) => {
                              return (
                                <div key={rtRowIndex}>
                                  {rtRow.columns.map((rtColumn, rtColumnIndex) => {
                                    return (
                                      <div key={rtColumnIndex}>
                                        {rtColumn.elements.map((element, rtElementIndex) => (
                                          <JsxParser
                                            key={`${rtRowIndex}_${rtColumnIndex}_${rtElementIndex}`}
                                            components={wcmElements}
                                            jsx={_columnValue(rowValue, element.name)}
                                          />
                                        ))}
                                      </div>
                                    );
                                  })}
                                </div>
                              );
                            })}
                          </div>
                        ))}
                      </div>
                    ))}
                  </div>
                ))}
            </div>
          );
        })}
    </>
  );
}
export default QueryResultRenderer;
