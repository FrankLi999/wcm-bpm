import Icon from '@material-ui/core/Icon';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles } from '@material-ui/core/styles';
import clsx from 'clsx';
import PropTypes from 'prop-types';
import React, { useMemo } from 'react';
import { useSelector } from 'react-redux';
import { withRouter } from 'react-router-dom';
import BpwUtils from '../../../utils';
import BpwNavBadge from '../BpwNavBadge';

const useStyles = makeStyles((theme) => ({
  root: {
    minHeight: 48,
    '&.active': {
      backgroundColor: theme.palette.secondary.main,
      color: `${theme.palette.secondary.contrastText}!important`,
      pointerEvents: 'none',
      '& .list-item-text-primary': {
        color: 'inherit',
      },
      '& .list-item-icon': {
        color: 'inherit',
      },
    },
    '& .list-item-icon': {},
    '& .list-item-text': {
      padding: '0 0 0 16px',
    },
    color: theme.palette.text.primary,
    textDecoration: 'none!important',
  },
}));

function BpwNavHorizontalLink(props) {
  const userRole = useSelector(({ auth }) => auth.user.roles);

  const classes = useStyles(props);
  const { item } = props;

  const hasPermission = useMemo(() => BpwUtils.hasPermission(item.auth, userRole), [item.auth, userRole]);

  if (!hasPermission) {
    return null;
  }

  return (
    <ListItem button component="a" href={item.url} target={item.target ? item.target : '_blank'} className={clsx('list-item', classes.root)} role="button">
      {item.icon && (
        <Icon className="list-item-icon text-16 flex-shrink-0" color="action">
          {item.icon}
        </Icon>
      )}

      <ListItemText className="list-item-text" primary={item.title} classes={{ primary: 'text-14 list-item-text-primary' }} />

      {item.badge && <BpwNavBadge className="ltr:ml-8 rtl:mr-8" badge={item.badge} />}
    </ListItem>
  );
}

BpwNavHorizontalLink.propTypes = {
  item: PropTypes.shape({
    id: PropTypes.string.isRequired,
    title: PropTypes.string,
    icon: PropTypes.string,
    url: PropTypes.string,
    target: PropTypes.string,
  }),
};

BpwNavHorizontalLink.defaultProps = {};

const NavHorizontalLink = withRouter(React.memo(BpwNavHorizontalLink));

export default NavHorizontalLink;
