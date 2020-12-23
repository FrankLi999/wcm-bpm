import ListSubheader from '@material-ui/core/ListSubheader';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import clsx from 'clsx';
import PropTypes from 'prop-types';
import React, { useMemo } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { withRouter } from 'react-router-dom';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import { navbarCloseMobile } from '../../../store/navbarSlice';
import NavLinkAdapter from '../../NavLinkAdapter';
import BpwUtils from '../../../utils';
import BpwNavItem from '../BpwNavItem';

const useStyles = makeStyles((theme) => ({
  item: (props) => ({
    height: 40,
    width: 'calc(100% - 16px)',
    borderRadius: '0 20px 20px 0',
    paddingRight: 12,
    paddingLeft: props.itemPadding > 80 ? 80 : props.itemPadding,
    '&.active > .list-subheader-text': {
      fontWeight: 700,
    },
  }),
}));

function BpwNavVerticalGroup(props) {
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
    <>
      <ListSubheader
        disableSticky
        className={clsx(classes.item, 'list-subheader flex items-center', !item.url && 'cursor-default')}
        onClick={(ev) => mdDown && dispatch(navbarCloseMobile())}
        component={item.url ? NavLinkAdapter : 'li'}
        to={item.url}
        role="button"
      >
        <span className="list-subheader-text uppercase text-12">{item.title}</span>
      </ListSubheader>

      {item.children && (
        <>
          {item.children.map((_item) => (
            <BpwNavItem key={_item.id} type={`vertical-${_item.type}`} item={_item} nestedLevel={nestedLevel} />
          ))}
        </>
      )}
    </>
  );
}

BpwNavVerticalGroup.propTypes = {
  item: PropTypes.shape({
    id: PropTypes.string.isRequired,
    title: PropTypes.string,
    children: PropTypes.array,
  }),
};

BpwNavVerticalGroup.defaultProps = {};

const NavVerticalGroup = withRouter(React.memo(BpwNavVerticalGroup));

export default NavVerticalGroup;
