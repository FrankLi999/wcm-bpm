import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import './nested_nav_item.dart';
import '../../theme/AppTheme.dart';
import '../../app_context/AppContext.dart';
import 'bloc/nav_bloc.dart';
import 'bloc/nav_repository.dart';

class CollapsibleDrawer extends StatefulWidget {
  final List<NestedNavigatorItem> items;
  final CustomAppTheme customAppTheme;
  const CollapsibleDrawer(
      {Key key, @required this.items, @required this.customAppTheme})
      : super(key: key);
  @override
  State<StatefulWidget> createState() => _CollapsibleDrawerState();
}

class _CollapsibleDrawerState extends State<CollapsibleDrawer> {
  NavBloc _navBloc;
  NavRepository _navRepository;
  @override
  void initState() {
    super.initState();
    _navRepository = RepositoryProvider.of<NavRepository>(context);
    _navBloc = BlocProvider.of<NavBloc>(context);
  }

  void _selectNavigator(key, context) {
    Scaffold.of(context).openEndDrawer();
    // NestedNavigatorsBlocProvider.of(context).actionWithScaffold(
    //   (scaffoldState) => scaffoldState.openEndDrawer(),
    // );
    if (key == '/auth/logout') {
      AppContext.authenticationService.logout(context);
    } else {
      _navBloc.add(NavEvent(navPath: key));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: Container(
          color: widget.customAppTheme.bgLayer1,
          child: ListView(
            children: _getMenuItems(widget.items, _navRepository.navPath,
                context, (key, context) => _selectNavigator(key, context)),
          )),
    );
  }

  List<Widget> _getMenuItems(
      List<NestedNavigatorItem> items,
      String selectedItemKey,
      BuildContext context,
      Function(String, BuildContext) selectNavigator) {
    final List<Widget> menuItems = [];
    menuItems.add(DrawerHeader(
      decoration: BoxDecoration(
        color: Colors.blueGrey,
      ),
      child: Center(
        child: Text(
          'Flutter MySite',
          style: TextStyle(color: Colors.white, fontSize: 20),
        ),
      ),
    ));
    // menuItems(ListTile(
    //   onTap: _goHome,
    //   title: Text(
    //     'Home',
    //     style: TextStyle(fontSize: 16),
    //   ),
    //   selected: _isHome,
    //   dense: true,
    // ));
    // ////////////////////////
    widget.items.forEach((item) {
      if (item.children == null || item.children.length == 0) {
        menuItems.add(
            _createListTile(item, selectedItemKey, context, selectNavigator));
      } else {
        menuItems.add(ExpansionTile(
          title: Text(
            item.text,
            style: TextStyle(fontSize: 18.0, fontWeight: FontWeight.bold),
          ),
          children:
              _getSubMenuItems(item, selectedItemKey, context, selectNavigator),
        ));
      }
    });
    // y
    return menuItems;
  }

  List<Widget> _getSubMenuItems(
      NestedNavigatorItem item,
      String selectedItemKey,
      BuildContext context,
      Function(String, BuildContext) selectNavigator) {
    final List<Widget> subMenuItems = [];
    subMenuItems
        .add(_createListTile(item, selectedItemKey, context, selectNavigator));
    subMenuItems.addAll(item.children
        .map((item) =>
            _createListTile(item, selectedItemKey, context, selectNavigator))
        .toList());
    return subMenuItems;
  }

  ListTile _createListTile(NestedNavigatorItem item, String selectedItemKey,
      BuildContext context, Function(String, BuildContext) selectNavigator) {
    return ListTile(
      title: Text(
        item.text,
        style: TextStyle(
          color: item.url == selectedItemKey ? Colors.blue : null,
        ),
      ),
      trailing: Icon(
        item.icon,
        color: item.url == selectedItemKey ? Colors.blue : null,
      ),
      onTap: () => selectNavigator(item.url, context),
    );
  }
}
