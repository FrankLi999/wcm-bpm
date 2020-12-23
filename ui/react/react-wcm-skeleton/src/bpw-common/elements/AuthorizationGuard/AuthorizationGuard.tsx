import React, { useState, useEffect, useContext } from 'react';
import { connect } from 'react-redux';
import { matchRoutes } from 'react-router-config';
import { withRouter } from 'react-router-dom';
import BpwUtils from '../../utils';
import AppContext from '../../AppContext';

function AuthorizationGuard(props) {
  const [accessGranted, setAccessGranted] = useState(true);
  const { routes } = useContext(AppContext);

  useEffect(() => {
    if (!getDerivedStateFromProps(props, routes)) {
      redirectRoute();
    }
  }, [props.location]);

  const getDerivedStateFromProps = (props, routes) => {
    const { location, userRole } = props;
    const { pathname } = location;
    const matched = matchRoutes(routes, pathname)[0];
    const hasPermission = matched ? BpwUtils.hasPermission(matched.route.auth, userRole) : true;
    setAccessGranted(hasPermission);
    return hasPermission;
  };

  const redirectRoute = () => {
    const { location, userRole, history } = props;
    const { pathname, state } = location;
    const redirectUrl = state && state.redirectUrl ? state.redirectUrl : '/';
    /*
        User is guest
        Redirect to Login Page
        */
    if (!userRole || userRole.length === 0) {
      history.push({
        pathname: '/auth/login',
        state: { redirectUrl: pathname },
      });
    } else {
      /*
        User is member
        User must be on unAuthorized page or just logged in
        Redirect to dashboard or redirectUrl
        */
      history.push({
        pathname: redirectUrl,
      });
    }
  };
  return accessGranted ? <>{props.children}</> : null;
}

function mapStateToProps({ auth }) {
  return {
    userRole: auth.user.roles,
  };
}

export default withRouter(connect(mapStateToProps)(AuthorizationGuard));
