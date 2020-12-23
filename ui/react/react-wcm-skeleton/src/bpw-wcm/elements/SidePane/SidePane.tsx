import React from 'react';
import { useLocation } from 'react-router-dom';
import QueryResultRenderer from '../QueryResultRenderer';
import ResourceRenderer from '../ResourceRenderer';
function useQuery() {
  return new URLSearchParams(useLocation().search);
}
function SidePane(props) {
  const layout = props.layout;
  const wcmSystem = props.wcmSystem;
  const query = useQuery();

  function sideViewerId(viewerIndex) {
    return `s_${viewerIndex}`;
  }
  function contentPathParam(viewer) {
    let paramName = viewer.contentParameter || 'contentPath';
    return query.get(paramName) ? query.get(paramName).split(',') : [];
  }

  return (
    <div style={{ 'flex-grow': layout.sidePane.width, 'flex-direction': 'column', 'justify-content': 'start', flex: '1 0 auto' }}>
      {layout.sidePane.viewers.map((viewer, viewerIndex) => {
        return (
          <div key={viewerIndex}>
            {wcmSystem.renderTemplates[layout.sidePane.viewers[viewerIndex].renderTemplate].query && (
              <QueryResultRenderer
                renderer={sideViewerId(viewerIndex)}
                repository={props.repository}
                workspace={props.workspace}
                siteAreaKey={props.siteAreaKey}
                rendererTemplate={wcmSystem.renderTemplates[layout.sidePane.viewers[viewerIndex].renderTemplate]}
              />
            )}
            {(layout.sidePane.viewers[viewerIndex].contentPath || contentPathParam(layout.sidePane.viewers[viewerIndex])) && (
              <>
                {(layout.sidePane.viewers[viewerIndex].contentPath || contentPathParam(layout.sidePane.viewers[viewerIndex])).map(
                  (contentPath, contentPathIndex) => {
                    return (
                      <ResourceRenderer
                        key={contentPathIndex}
                        renderer={sideViewerId(viewerIndex)}
                        repository={props.repository}
                        workspace={props.workspace}
                        siteAreaKey={props.siteAreaKey}
                        rendererTemplate={wcmSystem.renderTemplates[layout.sidePane.viewers[viewerIndex].renderTemplate]}
                        contentPath={contentPath}
                        contentPathIndex={contentPathIndex}
                      />
                    );
                  }
                )}
              </>
            )}
          </div>
        );
      })}
    </div>
  );
}

export default SidePane;
