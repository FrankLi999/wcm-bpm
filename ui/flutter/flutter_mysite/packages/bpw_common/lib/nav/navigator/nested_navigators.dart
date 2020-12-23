import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'bloc/nav_bloc.dart';
import 'bloc/nav_repository.dart';
import './nested_nav_item.dart';
import './bottom_nav_bar.dart';
import './collapsible_drawer.dart';
import '../../theme/AppTheme.dart';
import '../../app_context/AppContext.dart';
import '../../auth/components/auth_guard/auth_guard.dart';
import 'nav_utils.dart';

//AuthGuard
class NestedNavigators extends StatefulWidget {
  final String position;
  final List<NestedNavigatorItem> items;
  final String initialSelectedNavigatorKey;
  final DragStartBehavior drawerDragStartBehavior;
  final ThemeData bottomNavigationBarTheme;
  final double bottomNavigationBarElevation;
  final bool clearStackAfterTapOnCurrentTab;
  final CustomAppTheme customAppTheme;
  NestedNavigators({
    @required this.items,
    initialNavigatorKey,
    @required this.customAppTheme,
    this.position,
    this.drawerDragStartBehavior = DragStartBehavior.down,
    this.bottomNavigationBarTheme,
    this.bottomNavigationBarElevation = 8,
    this.clearStackAfterTapOnCurrentTab = true,
  })  : initialSelectedNavigatorKey =
            initialNavigatorKey != null ? initialNavigatorKey : items.first.url,
        assert(items.isNotEmpty);

  @override
  State<NestedNavigators> createState() => _NestedNavigatorsState();
}

class _NestedNavigatorsState extends State<NestedNavigators> {
  NavBloc _navBloc;
  NavRepository _navRepository;
  bool _hasBlocProviderInTree = false;
  GlobalKey<ScaffoldState> _scaffoldKey = new GlobalKey();

  List<NestedNavigatorItem> get _items => widget.items;
  NavigatorState _getNavigatorState(String key) {
    return _findNavigatorState(key, _items);
  }

  NavigatorState _findNavigatorState(key, List<NestedNavigatorItem> items) {
    NestedNavigatorItem item = NavUtils.findNestedNavItem(key, items);
    return (item == null) ? null : item.navigatorState;
  }

  @override
  void initState() {
    super.initState();
    _navRepository = RepositoryProvider.of<NavRepository>(context);
    _navBloc = BlocProvider.of<NavBloc>(context);
    _hasBlocProviderInTree = _navBloc != null;
    if (_navBloc == null) {
      _navBloc = new NavBloc(navRepository: _navRepository);
    }
    _navBloc.add(NavEvent(navPath: widget.initialSelectedNavigatorKey));
  }

  
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async =>
          !await _getNavigatorState(_navRepository.navPath).maybePop(),
      child: _hasBlocProviderInTree
          ? _buildScaffold()
          : BlocProvider<NavBloc>(
              create: (context) {
                return _navBloc;
              },
              child: _buildScaffold(),
            ),
    );
  }

  _buildScaffold() => BlocBuilder<NavBloc, NavState>(builder: (context, state) {
        return AuthGuard(
            items: _items,
            child: Scaffold(
              key: _scaffoldKey,
              drawer: widget.position == "drawer"
                  ? CollapsibleDrawer(
                      items: _items, customAppTheme: widget.customAppTheme)
                  : null,
              drawerDragStartBehavior: widget.drawerDragStartBehavior,
              body: // Stack(children: [
                  _buildNavigator(
                      NavUtils.findNestedNavItem(
                          _navRepository.navPath, _items),
                      _navRepository.navPath),
              //]),
              bottomNavigationBar: widget.position == 'bottom'
                  ? BottomNavBar(
                      items: widget.items,
                      bottomNavigationBarTheme: widget.bottomNavigationBarTheme,
                      bottomNavigationBarElevation:
                          widget.bottomNavigationBarElevation,
                      clearStackAfterTapOnCurrentTab:
                          widget.clearStackAfterTapOnCurrentTab)
                  : Container(height: 0),
            ));
      });

  Widget _buildNavigator(NestedNavigatorItem item, String currentKey) {
    // return item == null
    //     ? Offstage(
    //         offstage: true,
    //         child: AppContext.loadingScreen,
    //       )
    //     : Offstage(
    //         offstage: currentKey != item.url,
    //         child: item.navigator,
    //       );
    return item == null ? AppContext.loadingScreen : item.navigator;
  }
}
