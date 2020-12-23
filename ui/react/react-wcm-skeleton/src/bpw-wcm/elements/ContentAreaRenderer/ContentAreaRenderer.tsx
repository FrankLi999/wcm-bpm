import React from 'react';
import { makeStyles } from '@material-ui/core';
import { useLocation } from 'react-router-dom';
import QueryResultRenderer from '../QueryResultRenderer';
import ResourceRenderer from '../ResourceRenderer';
import JsxRenderer from '../JsxRenderer';
function useQuery() {
  return new URLSearchParams(useLocation().search);
}
const useStyles = makeStyles({
  'content-row': {
    'flex-grow': (props) => props.layout.contentWidth
  },
});
function ContentAreaRenderer(props) {
  const layout = props.layout;
  const classes = useStyles(props);
  const wcmSystem = props.wcmSystem;
  const query = useQuery();
  function contentRenderId(rowIndex, columnIndex, viewerIndex) {
    return `c_${rowIndex}_${columnIndex}_${viewerIndex}`;
  }

  function contentPathParam(viewer) {
    let paramName = viewer.contentParameter || 'contentPath';
    return query.get(paramName) ? query.get(paramName).split(',') : [];
  }

  function preloop(rt) {
    return wcmSystem.renderTemplates[rt].preloop;
  }

  function postloop(rt) {
    return wcmSystem.renderTemplates[rt].postloop;
  }

  function hasPreloop(rt) {
    return !!wcmSystem.renderTemplates[rt].preloop;
  }

  function hasPostloop(rt) {
    return !!wcmSystem.renderTemplates[rt].postloop;
  }
  return (
    <div className={(classes.contentRow, 'center', 'content-layout')}>
      {layout.rows.map((row, rowIndex) => {
        return (
          <div key={rowIndex} className="card, row-layout" style={{ flexDirection: 'row' }}>
            {row.columns.map((column, columnIndex) => {
              return (
                <div className="column-layout" key={columnIndex} style={{ flexDirection: 'column', width: `${column.width}%` }}>
                  {column.viewers.map((viewer, viewerIndex) => (
                    <div key={viewerIndex} className="card container">
                      {hasPreloop(viewer.renderTemplate) && <JsxRenderer jsx={preloop(viewer.renderTemplate)} />}

                      {wcmSystem.renderTemplates[layout.rows[rowIndex].columns[columnIndex].viewers[viewerIndex].renderTemplate].query && (
                        <QueryResultRenderer
                          repository={props.repository}
                          workspace={props.workspace}
                          siteAreaKey={props.siteAreaKey}
                          renderer={contentRenderId(rowIndex, columnIndex, viewerIndex)}
                          rendererTemplate={wcmSystem.renderTemplates[layout.rows[rowIndex].columns[columnIndex].viewers[viewerIndex].renderTemplate]}
                        />
                      )}
                      {(layout.rows[rowIndex].columns[columnIndex].viewers[viewerIndex].contentPath ||
                        contentPathParam(layout.rows[rowIndex].columns[columnIndex].viewers[viewerIndex])) && (
                        <div>
                          {(
                            layout.rows[rowIndex].columns[columnIndex].viewers[viewerIndex].contentPath ||
                            contentPathParam(layout.rows[rowIndex].columns[columnIndex].viewers[viewerIndex])
                          ).map((contentPath, contentPathIndex) => {
                            return (
                              <ResourceRenderer
                                key={contentPathIndex}
                                repository={props.repository}
                                workspace={props.workspace}
                                siteAreaKey={props.siteAreaKey}
                                renderer={contentRenderId(rowIndex, columnIndex, viewerIndex)}
                                rendererTemplate={wcmSystem.renderTemplates[layout.rows[rowIndex].columns[columnIndex].viewers[viewerIndex].renderTemplate]}
                                contentPath={contentPath}
                                contentPathIndex={contentPathIndex}
                              />
                            );
                          })}
                        </div>
                      )}

                      {hasPostloop(viewer.renderTemplate) && <JsxRenderer jsx={postloop(viewer.renderTemplate)} />}
                    </div>
                  ))}
                </div>
              );
            })}
          </div>
        );
      })}
    </div>
  );
}
export default ContentAreaRenderer;
