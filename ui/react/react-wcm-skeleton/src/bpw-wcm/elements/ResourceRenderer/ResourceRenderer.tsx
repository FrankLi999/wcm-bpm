import React, { useContext, useEffect, useState } from 'react';
import { tap } from 'rxjs/operators';
import AppContext from 'bpw-common/AppContext';
import JsxParser from 'react-jsx-parser';
import contentItemService from '../../services/ContentItemService';
function ResourceRenderer(props) {
  const { wcmElements } = useContext(AppContext);
  const { repository, workspace, siteAreaKey, rendererTemplate, renderer, contentPathIndex, contentPath } = props;
  const [contentItem, setContentItem] = useState(null);
  useEffect(() => {
    contentItemService
      .getContentItem(repository, workspace, contentPath)
      .pipe(tap((contentItem) => setContentItem(contentItem)))
      .subscribe();
    //todo, clean it up
  }, [contentPath]);

  function renderCode() {
    return rendererTemplate.code !== undefined;
  }

  function renderLayout() {
    return rendererTemplate.rows !== undefined;
  }

  function elementRenderContent(contentItem) {
    let result = `${rendererTemplate.code}`
      .replace('<render-element ', `<render-element content={${JSON.stringify(contentItem)}} `)
      .replace('<render-property ', `<render-property content={${JSON.stringify(contentItem)}} `);

    return _resolveWidgetBody(result);
  }

  function _resolveWidgetBody(content) {
    var regex = /\$\{.*?\}/g;
    let matches = content.match(regex);

    let result = content;
    if (matches) {
      for (let matche of matches) {
        let data = matche
          .substring(2, matche.length - 1)
          .trim()
          .split(':');
        if (data[0] === 'elements') {
          result = result.replace(matche, contentItem.elements[data[1]]);
        } else if (data[0] === 'properties') {
          result = result.replace(matche, contentItem.properties[data[1]]);
        }
      }
    }
    return result;
  }

  function _contentElement(contentItem, elementName) {
    return contentItem.elements[elementName] || contentItem.properties[elementName] || '';
  }
  return (
    <>
      {contentItem && renderCode() && <JsxParser components={wcmElements} jsx={elementRenderContent(contentItem)} />}
      {contentItem &&
        renderLayout() &&
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
                                    jsx={_contentElement(contentItem, element.name)}
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
    </>
  );
}
export default ResourceRenderer;
