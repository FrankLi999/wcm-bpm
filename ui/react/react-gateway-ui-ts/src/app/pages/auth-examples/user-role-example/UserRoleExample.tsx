import React from 'react';
import Button from '@material-ui/core/Button';
import Icon from '@material-ui/core/Icon';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import { useDispatch } from 'react-redux';
import BpwHighlight from 'bpw-common/elements/BpwHighlight';
import BpwPageSimple from 'bpw-common/elements/BpwPageSimple';
import logoutUser from 'bpw-auth/store/userSlice';

const useStyles = makeStyles((theme) => ({
  layoutRoot: {},
}));

function UserRoleExample(props) {
  const dispatch = useDispatch();

  const classes = useStyles();

  return (
    <BpwPageSimple
      classes={{
        root: classes.layoutRoot,
      }}
      header={
        <div className="flex flex-1 items-center justify-between p-24">
          <Typography className="h2">Staff: Auth role example page</Typography>
          <Button className="normal-case" variant="contained" onClick={(ev) => dispatch(logoutUser())}>
            <Icon>exit_to_app</Icon>
            <span className="mx-4">Logout</span>
          </Button>
        </div>
      }
      content={
        <div className="p-24">
          <Typography className="mb-24">
            You can see this page because you have logged in and have permission. Otherwise you should be redirected to login page.
          </Typography>

          <Typography className="mb-24">This is the page's config file:</Typography>

          <BpwHighlight component="pre" className="language-js">
            {`
                            import {authRoles} from 'auth';
                            import StaffRoleExample from 'app/main/auth/staff-role-example/StaffRoleExample';

                            export const StaffRoleExampleConfig = {
                                settings: {
                                    layout: {
                                        // config: {}
                                    }
                                },
                                auth    : authRoles.staff,//['admin',staff']
                                routes  : [
                                    {
                                        path     : '/auth/staff-role-example',
                                        component: StaffRoleExample
                                    }
                                ]
                            };
                            `}
          </BpwHighlight>

          <Typography className="my-24">You can also hide the navigation item/collapse/group with user roles by giving auth property.</Typography>

          <BpwHighlight component="pre" className="language-json">
            {`
                                export const appNavigationConfig = [
                                   {
                                        'id'   : 'only-staff-navigation-item',
                                        'title': 'Nav item only for Staff',
                                        'type' : 'item',
                                        'auth' : authRoles.staff,//['admin','staff']
                                        'url'  : '/auth/staff-role-example',
                                        'icon' : 'verified_user'
                                    }
                                ];
                            `}
          </BpwHighlight>
        </div>
      }
    />
  );
}

export default UserRoleExample;
