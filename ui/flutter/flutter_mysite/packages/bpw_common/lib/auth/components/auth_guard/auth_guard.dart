// import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import '../../../nav/navigator/bloc/nav_bloc.dart';
import '../../../nav/navigator/bloc/nav_repository.dart';
import '../../../nav/navigator/nested_nav_item.dart';
import '../../../nav/navigator/nav_utils.dart';
import '../../../app_context/AppContext.dart';
import "../../model/user_profile.dart";
import "../../bloc/repo/user_repository.dart";
import "../../bloc/auth/authentication_bloc.dart";

enum AuthorizationStatus { authenticated, authenticationRequired, guest }

class AuthGuard extends StatefulWidget {
  final Widget child;
  final List<NestedNavigatorItem> items;
  AuthGuard({@required this.child, @required this.items}) {
    assert(this.child != null);
  }

  @override
  _AuthGuardState createState() {
    return new _AuthGuardState();
  }
}

class _AuthGuardState extends State<AuthGuard> {
  UserRepository _userRepo;
  Widget currentWidget;
  NavBloc _navBloc;
  NavRepository _navRepository;
  @override
  void initState() {
    super.initState();
    currentWidget = AppContext.loadingScreen;
    _navBloc = BlocProvider.of<NavBloc>(context);
    _userRepo = RepositoryProvider.of<UserRepository>(context);
    _navRepository = RepositoryProvider.of<NavRepository>(context);
  }

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<AuthenticationBloc, AuthenticationState>(
        builder: (context, state) {
      if (state is AuthenticationUnintialized ||
          state is AuthenticationLoading) {
        currentWidget = AppContext.loadingScreen;
      } else {
        // state is AuthenticationAuthenticated or AuthenticationUnauthenticated
        AuthorizationStatus authorizationStatus =
            _checkAuthStatus(_userRepo.getUser());
        if (authorizationStatus == AuthorizationStatus.authenticationRequired) {
          _navBloc.add(NavEvent(
              navPath: '/auth/login',
              arguments: {'referer': _navRepository.navPath}));
          currentWidget = AppContext.loadingScreen;
        } else {
          currentWidget = widget.child;
        }
      }
      return currentWidget;
    });
  }

  AuthorizationStatus _checkAuthStatus(User user) {
    List<String> auth = _getAuthRoles();
    AuthorizationStatus status = AuthorizationStatus.authenticationRequired;
    if (auth == null || auth.length == 0) {
      status = AuthorizationStatus.guest;
    } else {
      if (user == null) {
        status = _guestPage(auth)
            ? AuthorizationStatus.guest
            : AuthorizationStatus.authenticationRequired;
      } else {
        status = _authenticatedPage(auth)
            ? AuthorizationStatus.authenticated
            : _checkUserRoles(auth, user.roles);
      }
    }
    return status;
  }

  List<String> _getAuthRoles() {
    NestedNavigatorItem item =
        NavUtils.findNestedNavItem(_navRepository.navPath, widget.items);
    return (item == null) ? null : item.auth;
  }

  bool _guestPage(List<String> auth) {
    return auth == null || auth.contains("guest");
  }

  bool _authenticatedPage(List<String> auth) {
    return auth == null || auth.contains("authenticated");
  }

  AuthorizationStatus _checkUserRoles(List<String> auth, List<String> roles) {
    AuthorizationStatus status = AuthorizationStatus.guest;
    if (!_guestPage(auth)) {
      status = AuthorizationStatus.authenticationRequired;
      for (var i = 0; i < auth.length; i++) {
        if (roles.contains(auth[i])) {
          status = AuthorizationStatus.authenticated;
          break;
        }
      }
    }
    return status;
  }
}
