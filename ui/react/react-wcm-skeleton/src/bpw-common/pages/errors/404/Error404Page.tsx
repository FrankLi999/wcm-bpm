import Icon from '@material-ui/core/Icon';
import Input from '@material-ui/core/Input';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import React from 'react';
import { Link } from 'react-router-dom';
import BpwAnimate from '../../../elements/BpwAnimate';
function Error404Page() {
  return (
    <div className="flex flex-col flex-1 items-center justify-center p-16">
      <div className="max-w-512 text-center">
        <BpwAnimate animation="transition.expandIn" delay={100}>
          <Typography variant="h1" color="inherit" className="font-medium mb-16">
            404
          </Typography>
        </BpwAnimate>

        <BpwAnimate delay={500}>
          <Typography variant="h5" color="textSecondary" className="mb-16">
            Sorry but we could not find the page you are looking for
          </Typography>
        </BpwAnimate>

        <Paper className="flex items-center w-full h-56 p-16 mt-48 mb-16" elevation={1}>
          <Icon color="action">search</Icon>
          <Input
            placeholder="Search for anything"
            className="px-16"
            disableUnderline
            fullWidth
            inputProps={{
              'aria-label': 'Search',
            }}
          />
        </Paper>

        <Link className="font-medium" to="/">
          Go back to home page
        </Link>
      </div>
    </div>
  );
}

export default Error404Page;
