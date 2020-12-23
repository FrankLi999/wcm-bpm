import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import './nested_nav_item.dart';
import '../../app_context/AppContext.dart';
import 'bloc/nav_bloc.dart';
import 'bloc/nav_repository.dart';

class BottomNavBar extends StatefulWidget {
  final List<NestedNavigatorItem> items;
  final ThemeData bottomNavigationBarTheme;
  final double bottomNavigationBarElevation;
  final bool clearStackAfterTapOnCurrentTab;

  const BottomNavBar(
      {Key key,
      @required this.items,
      this.bottomNavigationBarTheme,
      this.bottomNavigationBarElevation,
      this.clearStackAfterTapOnCurrentTab})
      : super(key: key);
  @override
  State<StatefulWidget> createState() => _BottomNavBarState();
}

class _BottomNavBarState extends State<BottomNavBar> {
  NavBloc _navBloc;
  NavRepository _navRepository;
  _BottomNavBarState();
  @override
  void initState() {
    super.initState();
    _navRepository = RepositoryProvider.of<NavRepository>(context);
    _navBloc = BlocProvider.of<NavBloc>(context);
  }

  @override
  Widget build(BuildContext context) {
    return Theme(
        data: widget.bottomNavigationBarTheme != null
            ? widget.bottomNavigationBarTheme
            : Theme.of(context),
        child: _buildNativeBottomNavigatorBar(context));
  }

  NavigatorState _getNavigatorState(String key) =>
      widget.items.firstWhere((item) => item.url == key).navigatorState;
  String _getNavigatorKeyByIndex(int index) =>
      widget.items.elementAt(index).url;

  int _getNavigatorIndexByKey(String key) =>
      widget.items.indexWhere((item) => item.url == key);

  Widget _buildNativeBottomNavigatorBar(BuildContext context) =>
      BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        elevation: widget.bottomNavigationBarElevation,
        items: _getBottomNavigatorBarItems(),
        currentIndex: _getNavigatorIndexByKey(_navRepository.navPath),
        onTap: (index) =>
            _onTabBarItemClick(_getNavigatorKeyByIndex(index), context),
      );

  List<BottomNavigationBarItem> _getBottomNavigatorBarItems() {
    return widget.items
        .map(
          (item) => _buildBottomNavigationItem(
            item,
            _navRepository.navPath == item.url,
          ),
        )
        .toList();
  }

  BottomNavigationBarItem _buildBottomNavigationItem(
      NestedNavigatorItem item, bool selected) {
    return BottomNavigationBarItem(
      icon: Icon(
        item.icon,
        color: Colors.blue,
      ),
      label: item.text,
    );
  }

  _onTabBarItemClick(String key, BuildContext context) {
    if (key == '/auth/logout') {
      AppContext.authenticationService.logout(context);
    } else if (_navRepository.navPath == key &&
        widget.clearStackAfterTapOnCurrentTab) {
      _getNavigatorState(key).popUntil((route) => route.isFirst);
    } else {
      _navBloc.add(NavEvent(navPath: key));
    }
  }
}
