import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import DemoContent from 'bpw-common/elements/DemoContent';
import BpwPageSimple from 'bpw-common/elements/BpwPageSimple';

const useStyles = makeStyles((theme) => ({
  layoutRoot: {},
}));

function ExamplePage(props) {
  const classes = useStyles(props);
  const { t } = useTranslation('examplePage');

  return (
    <BpwPageSimple
      classes={{
        root: classes.layoutRoot,
      }}
      header={
        <div className="p-24">
          <h4>{t('TITLE')}</h4>
        </div>
      }
      contentToolbar={
        <div className="px-24">
          <h4>Content Toolbar</h4>
        </div>
      }
      content={
        <div className="p-24">
          <h4>Content</h4>
          <br />
          <DemoContent />
        </div>
      }
    />
  );
}

export default ExamplePage;
