import React from 'react';
import { Redirect } from 'react-router-dom';
import BpwUtils from 'bpw-common/utils';
import ExampleConfig from '../main/example/ExampleConfig';
const routeConfigs = [ExampleConfig];

const routes = [
  ...BpwUtils.generateRoutesFromConfigs(routeConfigs),
  {
    path: '/',
    component: () => <Redirect to="/example" />,
  },
];

export default routes;
