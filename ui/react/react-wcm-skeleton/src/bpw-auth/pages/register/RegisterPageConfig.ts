import React from 'react';

const RegisterPageConfig = {
  settings: {
    layout: {
      // config: {},
    },
  },
  routes: [
    {
      path: '/auth/register',
      component: React.lazy(() => import('./RegisterPage')),
    },
  ],
};

export default RegisterPageConfig;
