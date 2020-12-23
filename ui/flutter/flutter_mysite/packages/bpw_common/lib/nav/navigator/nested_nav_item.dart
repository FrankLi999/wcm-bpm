import 'package:flutter/material.dart';
import '../../app_context/AppContext.dart';

class NestedNavigatorItem {
  NestedNavigatorItem(
      {@required this.url,
      this.icon,
      this.text,
      this.auth,
      this.expanded: false,
      this.selected: false,
      this.children}) {
    // _navigatorObserver = _NestedNavigatorObserver(); //_navigatorKey);
    _heroController = HeroController(createRectTween: _createRectTween);

    _navigator = new Navigator(
        // observers: <NavigatorObserver>[_navigatorObserver, _heroController],
        observers: <NavigatorObserver>[_heroController],
        key: _navigatorKey,
        onGenerateRoute: (routeSettings) {
          return AppContext.generateRoute(
              RouteSettings(name: url, arguments: routeSettings.arguments));
          // return routeSettings.name == _rootRouteName
          //     ? AppContext.generateRoute(
          //         RouteSettings(name: url, arguments: routeSettings.arguments))
          //     : AppContext.generateRoute(routeSettings);
        });
  }

  final IconData icon;
  final String text;
  final String url;
  final List<String> auth;
  bool expanded;
  bool selected;
  final List<NestedNavigatorItem> children;
  final _navigatorKey = GlobalKey<NavigatorState>();
  Navigator _navigator;
  // _NestedNavigatorObserver _navigatorObserver;
  HeroController _heroController;

  Navigator get navigator => _navigator;

  NavigatorState get navigatorState => _navigatorKey.currentState;

  RectTween _createRectTween(Rect begin, Rect end) {
    return MaterialRectArcTween(begin: begin, end: end);
  }
}

// class _NestedNavigatorObserver extends NavigatorObserver {
//   bool navTabBarVisible = true;

//   _NestedNavigatorObserver();
//   @override
//   void didReplace({Route<dynamic> newRoute, Route<dynamic> oldRoute}) {
//     updateNavTabBarVisibility(newRoute, oldRoute);
//   }

//   @override
//   void didRemove(Route<dynamic> route, Route<dynamic> previousRoute) {
//     updateNavTabBarVisibility(route, previousRoute);
//   }

//   @override
//   void didPop(Route<dynamic> route, Route<dynamic> previousRoute) {
//     updateNavTabBarVisibility(route, previousRoute);
//   }

//   @override
//   void didPush(Route<dynamic> route, Route<dynamic> previousRoute) {
//     updateNavTabBarVisibility(route, previousRoute);
//   }

//   void updateNavTabBarVisibility(
//       Route<dynamic> route, Route<dynamic> previousRoute) {
//     Map<String, dynamic> arguments = Map();
//     if (route != null &&
//         route.isCurrent &&
//         route.settings.arguments is Map<String, dynamic>) {
//       arguments.addAll(route.settings.arguments);
//     } else if (previousRoute != null &&
//         previousRoute.isCurrent &&
//         previousRoute.settings.arguments is Map<String, dynamic>) {
//       arguments.addAll(previousRoute.settings.arguments);
//     }
//   }
// }
