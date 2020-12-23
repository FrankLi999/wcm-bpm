import React from 'react';

const LockPageConfig = {
  settings: {
    layout: {
      // config: {
        navbar: {
          display: false,
        },
        toolbar: {
          display: false,
        },
        footer: {
          display: false,
        },
        leftSidePanel: {
          display: false,
        },
        rightSidePanel: {
          display: false,
        },
      // },
    },
  },
  routes: [
    {
      path: '/auth/lock',
      component: React.lazy(() => import('./LockPage')),
    },
  ],
};

export default LockPageConfig;
