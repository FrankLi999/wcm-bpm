import React from 'react';

const MailConfirmPageConfig = {
  settings: {
    layout: {
      // config: {},
    },
  },
  routes: [
    {
      path: '/auth/mail-confirm',
      component: React.lazy(() => import('./MailConfirmPage')),
    },
  ],
};

export default MailConfirmPageConfig;
