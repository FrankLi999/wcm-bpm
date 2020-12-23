import React from 'react';
import { Redirect } from 'react-router-dom';
import BpwUtils from 'bpw-common/utils';
import GatewayConfig from 'app/pages/gateway/config/GatewayConfig';
import authRoleExamplesConfigs from 'app/pages/auth-examples/config/authRoleExamplesConfigs';
import authPagesConfigs from './authPagesConfigs';
import commonPagesConfigs from 'bpw-common/pages/commonPagesConfigs';
const routeConfigs = [GatewayConfig, ...authRoleExamplesConfigs, ...authPagesConfigs, ...commonPagesConfigs];

const routes = [
  ...BpwUtils.generateRoutesFromConfigs(routeConfigs),
  {
    path: '/*',
    component: () => <Redirect to="/apigateway/home" />,
  },
];

export default routes;
