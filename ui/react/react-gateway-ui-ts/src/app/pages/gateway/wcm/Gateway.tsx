import React from 'react';
import { useSelector } from 'react-redux';
import WcmPage from 'bpw-wcm/elements/WcmPage';

function GatewayPage(props) {
    const wcmSystem = useSelector(({ wcm }) => wcm.wcmSystem);
    return wcmSystem && wcmSystem.siteConfig ? <WcmPage /> : null;
}

export default GatewayPage;
