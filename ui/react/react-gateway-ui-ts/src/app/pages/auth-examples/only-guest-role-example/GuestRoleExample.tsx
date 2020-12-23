import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import React from 'react';
import BpwHighlight from 'bpw-common/elements/BpwHighlight';
import BpwPageSimple from 'bpw-common/elements/BpwPageSimple';
const useStyles = makeStyles((theme) => ({
  layoutRoot: {},
}));

function GuestRoleExample() {
  const classes = useStyles();

  return (
    <BpwPageSimple
      classes={{
        root: classes.layoutRoot,
      }}
      header={
        <div className="p-24 flex items-center">
          <Typography className="h2">Guest: Auth role example page</Typography>
        </div>
      }
      content={
        <div className="p-24">
          <Typography className="mb-24">You can see this page because you have not logged in. Otherwise you should be redirected to root page.</Typography>

          <Typography className="mb-24">This is the page's config file:</Typography>

          <BpwHighlight component="pre" className="language-js">
            {`
                            import {authRoles} from 'auth';
                            import StaffRoleExample from 'app/main/auth/staff-role-example/StaffRoleExample';

                            export const GuestRoleExampleConfig = {
                                settings: {
                                    layout: {
                                        // config: {}
                                    }
                                },
                                auth    : authRoles.onlyGuest,//['guest']
                                routes  : [
                                    {
                                        path     : '/auth/guest-role-example',
                                        component: GuestRoleExample
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
                                        'title': 'Nav item only for Guest',
                                        'type' : 'item',
                                        'auth' : authRoles.onlyGuest,//['guest']
                                        'url'  : '/auth/guest-role-example',
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

export default GuestRoleExample;
