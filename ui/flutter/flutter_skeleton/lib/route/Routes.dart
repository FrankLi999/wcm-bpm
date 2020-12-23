import 'package:flutter/material.dart';
import 'package:bpw_wcm/screens/WcmScreen.dart';
import 'package:bpw_common/screens/UnknownScreen.dart';
import 'package:bpw_common/auth/screens/login/LoginScreen.dart';
import 'package:bpw_common/auth/screens/registration/RegistrationScreen.dart';
import 'package:bpw_common/auth/screens/reset_password/ResetPasswordScreen.dart';

class Routes {
  Routes._();

  static MaterialPageRoute generateRoute(RouteSettings settings) {
    // Handle '/'
    if (settings.name == '/') {
      return MaterialPageRoute(
          builder: (context) => WcmScreen(wcmPath: "/mysite/home"));
    }

    var uri = Uri.parse(settings.name);
    if (uri.pathSegments.length >= 2 && uri.pathSegments.first == 'mysite') {
      // WCM Path
      return MaterialPageRoute(
          builder: (context) => WcmScreen(wcmPath: settings.name));
    }
    return MaterialPageRoute(
      settings: settings,
      builder: (context) => _buildPage(settings.name, settings.arguments),
    );
  }

  static Widget _buildPage(String name, Object arguments) {
    // Map<String, dynamic> argumentsMap =
    //     arguments is Map<String, dynamic> ? arguments : Map();
    switch (name) {
      case "/auth/login":
        return LoginScreen();
      case "/auth/registration":
        return RegistrationScreen();
      case "/auth/reset_password":
        return ResetPasswordScreen();
      default:
        return UnknownScreen();
    }
  }
}
