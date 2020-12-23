import React from 'react';
import { useTranslation } from 'react-i18next';

function ApiGatewayWelcome(props) {
  const { t } = useTranslation('apigateway');
  return (
    <div style={{ display: 'flex' }}>
      <img
        src="/assets/images/demo-content/morain-lake.jpg"
        alt="beach"
        style={{
          maxWidth: '640px',
          width: '100%',
        }}
        className="rounded-6"
      />
      <p>{t(props.message)}</p>
    </div>
  );
}

export default ApiGatewayWelcome;
