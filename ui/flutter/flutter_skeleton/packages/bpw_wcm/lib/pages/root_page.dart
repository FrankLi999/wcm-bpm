import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_i18n/flutter_i18n.dart';
import 'package:bpw_common/nav/navigator/nested_navigators.dart';
import 'package:bpw_common/nav/navigator/nested_nav_item.dart';
import 'package:bpw_common/auth/bloc/repo/user_repository.dart';
import 'package:bpw_common/theme/AppTheme.dart';
import 'package:bpw_common/app_context/AppContext.dart';
import 'package:bpw_common/nav/navigator/bloc/nav_bloc.dart';
import 'package:bpw_common/utils/SizeConfig.dart';
import '../bloc/site_config/site_config_repository.dart';
import '../bloc/site_config/site_config_bloc.dart';

class RootPage extends StatefulWidget {
  final ThemeData themeData;
  final CustomAppTheme customAppTheme;
  RootPage({Key key, this.themeData, this.customAppTheme}) : super(key: key);
  @override
  State<StatefulWidget> createState() => _RootPageState();
}

class _RootPageState extends State<RootPage> {
  SiteConfigRepository _siteConfigRepository;
  UserRepository _userRepository;
  Widget currentWidget;
  @override
  void initState() {
    super.initState();
    currentWidget = AppContext.loadingScreen;
    _siteConfigRepository =
        RepositoryProvider.of<SiteConfigRepository>(context);
    _userRepository = RepositoryProvider.of<UserRepository>(context);
  }

  @override
  Widget build(BuildContext context) {
    MySize().init(context);
    return BlocBuilder<SiteConfigBloc, SiteConfigState>(
        builder: (context, state) {
      if (state is ProtectedSiteConfigUpdated ||
          state is PublicSiteConfigUpdated) {
        currentWidget = ("drawer" == _navigatorPosition())
            ? NestedNavigators(
                items: _getNavigationItems(context),
                customAppTheme: widget.customAppTheme,
                position: "drawer",
                initialNavigatorKey: _getInitialNavigatorKey(context))
            : NestedNavigators(
                items: _getNavigationItems(context),
                position: "bottom",
                customAppTheme: widget.customAppTheme,
                bottomNavigationBarTheme: Theme.of(context).copyWith(
                  splashColor: Colors.blue[100],
                ),
                initialNavigatorKey: _getInitialNavigatorKey(context));
      } else {
        currentWidget = AppContext.loadingScreen;
      }
      return currentWidget;
    });
  }

  String _getInitialNavigatorKey(BuildContext context) {
    return BlocProvider.of<NavBloc>(context).state.navPath ?? "/mysite/home";
  }

  bool _shouldAddNavigationItem(dynamic entry) {
    List<String> roles = _userRepository.getUser() == null
        ? []
        : _userRepository.getUser().roles;
    List<String> auth =
        (entry["auth"] == null) ? [] : entry["auth"].cast<String>();
    if (roles.length == 0) {
      //guest user
      return auth.length == 0 ||
          auth.contains("guest") ||
          auth.contains("guestLink");
    } else if (auth.length == 0 || auth.contains("authenticated")) {
      return true;
    } else {
      for (var i = 0; i < auth.length; i++) {
        if (roles.contains(auth[i])) {
          return true;
        }
      }
      return false;
    }
  }

  String _navigatorPosition() {
    final position = _siteConfigRepository.siteConfigMap["siteConfig"]["layout"]
        ["navbar"]["position"];
    return (position == null) ? "drawer" : position;
  }

  NestedNavigatorItem _getNestedNavigatorItem(
      BuildContext context, dynamic entry) {
    final List<NestedNavigatorItem> childItems =
        _populateNavigationItems(context, (entry["children"]));
    return NestedNavigatorItem(
        url: entry["url"],
        expanded: false,
        selected: false,
        auth: ((entry["auth"] == null) ? [] : entry["auth"].cast<String>()),
        icon: AppContext.iconMap[entry["icon"]],
        text: FlutterI18n.translate(context, entry["translate"]),
        children: childItems);
  }

  List<NestedNavigatorItem> _populateNavigationItems(
      BuildContext context, List<dynamic> entries) {
    final List<NestedNavigatorItem> navigationItems = [];
    entries.forEach((entry) {
      if (_shouldAddNavigationItem(entry)) {
        navigationItems.add(_getNestedNavigatorItem(context, entry));
      }
    });
    return navigationItems;
  }

  List<NestedNavigatorItem> _getNavigationItems(BuildContext context) {
    final List<NestedNavigatorItem> appNavItem = _populateNavigationItems(
        context, _siteConfigRepository.appNavigationMap["navigations"]);
    final List<NestedNavigatorItem> siteNavItem = _populateNavigationItems(
        context, _siteConfigRepository.siteConfigMap["navigations"]);
    final List<NestedNavigatorItem> navigationItems = [
      ...appNavItem,
      ...siteNavItem,
    ];
    return navigationItems;
  }
}
