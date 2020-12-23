import React from 'react';

const ForgotPasswordPageConfig = {
  settings: {
    layout: {
      // config: {},
    },
  },
  routes: [
    {
      path: '/auth/forgot-password',
      component: React.lazy(() => import('./ForgotPasswordPage')),
    },
  ],
};

export default ForgotPasswordPageConfig;
