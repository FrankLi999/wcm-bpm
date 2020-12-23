import React from 'react';
import { useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom';
import SidePane from '../SidePane';
import ContentAreaRenderer from '../ContentAreaRenderer';
import _ from 'bpw-common/lodash';
function WcmPage(props) {
  const wcmSystem = useSelector(({ wcm }) => wcm.wcmSystem);
  const location = useLocation(); 
  const repository = wcmSystem.siteConfig.repository;
  const workspace = wcmSystem.siteConfig.workspace;
  const siteArea = wcmSystem.siteAreas[location.pathname.split('?')[0].trim().replace(/\//g, '~')];
  let layout = _.cloneDeep(wcmSystem.contentAreaLayouts[siteArea.elements.contentAreaLayout] || { name: '' });

  if (siteArea.siteAreaLayout) {
    layout.sidePane = _.cloneDeep(siteArea.siteAreaLayout.sidePane);
    layout.rows = _.cloneDeep(siteArea.siteAreaLayout.rows);
    layout.contentWidth = siteArea.siteAreaLayout.contentWidth;
  }
  function leftSidePane() {
    return layout.sidePane.left && layout.sidePane.width > 0;
  }
  function rightSidePane() {
    return !layout.sidePane.left && layout.sidePane.width > 0;
  }

  function getSiteAreaKey(siteArea) {
    return siteArea.elements['url'].replace(/\//g, '~');
  }
  return (
    <div id="wcmPage" className="page-layout carded left-sidebar inner-scroll">
      {leftSidePane() && (
        <SidePane layout={layout} wcmSystem={wcmSystem} repository={repository} workspace={workspace} siteAreaKey={getSiteAreaKey(siteArea)} />
      )}
      <ContentAreaRenderer layout={layout} wcmSystem={wcmSystem} repository={repository} workspace={workspace} siteAreaKey={getSiteAreaKey(siteArea)} />
      {rightSidePane() && (
        <SidePane layout={layout} wcmSystem={wcmSystem} repository={repository} workspace={workspace} siteAreaKey={getSiteAreaKey(siteArea)} />
      )}
    </div>
  );
}

export default WcmPage;
