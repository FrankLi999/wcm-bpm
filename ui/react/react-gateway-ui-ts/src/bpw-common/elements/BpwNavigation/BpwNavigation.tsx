import Divider from '@material-ui/core/Divider';
import List from '@material-ui/core/List';
import { makeStyles } from '@material-ui/core/styles';
import clsx from 'clsx';
import PropTypes from 'prop-types';
import React from 'react';
import BpwNavHorizontalCollapse from './horizontal/BpwNavHorizontalCollapse';
import BpwNavHorizontalGroup from './horizontal/BpwNavHorizontalGroup';
import BpwNavHorizontalItem from './horizontal/BpwNavHorizontalItem';
import BpwNavHorizontalLink from './horizontal/BpwNavHorizontalLink';
import BpwNavVerticalCollapse from './vertical/BpwNavVerticalCollapse';
import BpwNavVerticalGroup from './vertical/BpwNavVerticalGroup';
import BpwNavVerticalItem from './vertical/BpwNavVerticalItem';
import BpwNavVerticalLink from './vertical/BpwNavVerticalLink';
import BpwNavItem, { registerComponent } from './BpwNavItem';

/*
Register Bpw Navigation Components
 */
registerComponent('vertical-group', BpwNavVerticalGroup);
registerComponent('vertical-collapse', BpwNavVerticalCollapse);
registerComponent('vertical-item', BpwNavVerticalItem);
registerComponent('vertical-link', BpwNavVerticalLink);
registerComponent('horizontal-group', BpwNavHorizontalGroup);
registerComponent('horizontal-collapse', BpwNavHorizontalCollapse);
registerComponent('horizontal-item', BpwNavHorizontalItem);
registerComponent('horizontal-link', BpwNavHorizontalLink);
registerComponent('vertical-divider', () => <Divider className="my-16" />);
registerComponent('horizontal-divider', () => <Divider className="my-16" />);

const useStyles = makeStyles((theme) => ({
  navigation: {
    '& .list-item': {
      '&:hover': {
        backgroundColor: theme.palette.type === 'dark' ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0,0,0,.04)',
      },
      '&:focus:not(.active)': {
        backgroundColor: theme.palette.type === 'dark' ? 'rgba(255, 255, 255, 0.06)' : 'rgba(0,0,0,.05)',
      },
    },
  },
  verticalNavigation: {
    '&.active-square-list': {
      '& .list-item, & .active.list-item': {
        width: '100%',
        borderRadius: '0',
      },
    },
    '&.dense': {
      '& .list-item': {
        paddingTop: 0,
        paddingBottom: 0,
        height: 32,
      },
    },
  },
  horizontalNavigation: {
    '&.active-square-list': {
      '& .list-item': {
        borderRadius: '0',
      },
    },
    '& .list-item': {
      padding: '8px 12px 8px 12px',
      height: 40,
      minHeight: 40,
      '&.level-0': {
        height: 44,
        minHeight: 44,
      },
      '& .list-item-text': {
        padding: '0 0 0 8px',
      },
    },
  },
  '@global': {
    '.popper-navigation-list': {
      '& .list-item': {
        padding: '8px 12px 8px 12px',
        height: 40,
        minHeight: 40,
        '& .list-item-text': {
          padding: '0 0 0 8px',
        },
      },
      '&.dense': {
        '& .list-item': {
          minHeight: 32,
          height: 32,
          '& .list-item-text': {
            padding: '0 0 0 8px',
          },
        },
      },
    },
  },
}));

function BpwNavigation(props) {
  const classes = useStyles(props);
  const { navigation, layout, active, dense, className } = props;

  const verticalNav = (
    <List
      className={clsx('navigation whitespace-no-wrap', classes.navigation, classes.verticalNavigation, `active-${active}-list`, dense && 'dense', className)}
    >
      {navigation.map((_item) => (
        <BpwNavItem key={_item.id} type={`vertical-${_item.type}`} item={_item} nestedLevel={0} />
      ))}
    </List>
  );

  const horizontalNav = (
    <List
      className={clsx(
        'navigation whitespace-no-wrap flex p-0',
        classes.navigation,
        classes.horizontalNavigation,
        `active-${active}-list`,
        dense && 'dense',
        className
      )}
    >
      {navigation.map((_item) => (
        <BpwNavItem key={_item.id} type={`horizontal-${_item.type}`} item={_item} nestedLevel={0} dense={dense} />
      ))}
    </List>
  );

  if (navigation.length > 0) {
    switch (layout) {
      case 'horizontal': {
        return horizontalNav;
      }
      case 'vertical':
      default: {
        return verticalNav;
      }
    }
  } else {
    return null;
  }
}

BpwNavigation.propTypes = {
  navigation: PropTypes.array.isRequired,
};

BpwNavigation.defaultProps = {
  layout: 'vertical',
};

export default React.memo(BpwNavigation);
