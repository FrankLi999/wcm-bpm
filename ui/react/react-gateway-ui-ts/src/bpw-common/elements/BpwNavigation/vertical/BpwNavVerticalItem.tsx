import Icon from '@material-ui/core/Icon';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import clsx from 'clsx';
import PropTypes from 'prop-types';
import React, { useMemo } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { withRouter } from 'react-router-dom';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import NavLinkAdapter from '../../NavLinkAdapter';
import BpwUtils from '../../../utils';
import { navbarCloseMobile } from '../../../store/navbarSlice';
import BpwNavBadge from '../BpwNavBadge';

const useStyles = makeStyles((theme) => ({
  item: (props) => ({
    height: 40,
    width: 'calc(100% - 16px)',
    borderRadius: '0 20px 20px 0',
    paddingRight: 12,
    paddingLeft: props.itemPadding > 80 ? 80 : props.itemPadding,
    '&.active': {
      backgroundColor: theme.palette.secondary.main,
      color: `${theme.palette.secondary.contrastText}!important`,
      pointerEvents: 'none',
      transition: 'border-radius .15s cubic-bezier(0.4,0.0,0.2,1)',
      '& .list-item-text-primary': {
        color: 'inherit',
      },
      '& .list-item-icon': {
        color: 'inherit',
      },
    },
    '& .list-item-icon': {
      marginRight: 16,
    },
    '& .list-item-text': {},
    color: theme.palette.text.primary,
    cursor: 'pointer',
    textDecoration: 'none!important',
  }),
}));

function BpwNavVerticalItem(props) {
  const userRole = useSelector(({ auth }) => auth.user.roles);
  const dispatch = useDispatch();

  const theme = useTheme();
  const mdDown = useMediaQuery(theme.breakpoints.down('md'));
  const { item, nestedLevel } = props;
  const classes = useStyles({
    itemPadding: nestedLevel > 0 ? 40 + nestedLevel * 16 : 24,
  });

  const hasPermission = useMemo(() => BpwUtils.hasPermission(item.auth, userRole), [item.auth, userRole]);

  if (!hasPermission) {
    return null;
  }

  return (
    <ListItem
      button
      component={NavLinkAdapter}
      to={item.url}
      activeClassName="active"
      className={clsx(classes.item, 'list-item')}
      onClick={(ev) => mdDown && dispatch(navbarCloseMobile())}
      exact={item.exact}
    >
      {item.icon && (
        <Icon className="list-item-icon text-16 flex-shrink-0" color="action">
          {item.icon}
        </Icon>
      )}

      <ListItemText className="list-item-text" primary={item.title} classes={{ primary: 'text-14 list-item-text-primary' }} />

      {item.badge && <BpwNavBadge badge={item.badge} />}
    </ListItem>
  );
}

BpwNavVerticalItem.propTypes = {
  item: PropTypes.shape({
    id: PropTypes.string.isRequired,
    title: PropTypes.string,
    icon: PropTypes.string,
    url: PropTypes.string,
  }),
};

BpwNavVerticalItem.defaultProps = {};

const NavVerticalItem = withRouter(React.memo(BpwNavVerticalItem));

export default NavVerticalItem;
