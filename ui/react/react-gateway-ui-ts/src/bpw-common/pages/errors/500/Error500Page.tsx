import Typography from '@material-ui/core/Typography';
import React from 'react';
import { Link } from 'react-router-dom';
import BpwAnimate from '../../../elements/BpwAnimate';
function Error500Page() {
  return (
    <div className="flex flex-col flex-1 items-center justify-center p-16">
      <div className="max-w-512 text-center">
        <BpwAnimate animation="transition.expandIn" delay={100}>
          <Typography variant="h1" color="inherit" className="font-medium mb-16">
            500
          </Typography>
        </BpwAnimate>

        <BpwAnimate delay={600}>
          <Typography variant="subtitle1" color="textSecondary" className="mb-48">
            Looks like we have an internal issue, please try again in couple minutes
          </Typography>
        </BpwAnimate>

        <Link className="font-medium" to="/">
          Go back to home page
        </Link>
      </div>
    </div>
  );
}

export default Error500Page;
