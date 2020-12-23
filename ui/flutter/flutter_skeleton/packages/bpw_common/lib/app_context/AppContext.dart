// import 'dart:convert';
import 'package:flutter/material.dart';
// import 'package:flutter/services.dart';
// import 'package:shared_preferences/shared_preferences.dart';
import "../screens/LoadingScreen.dart";

class AppContext {
  // static Map<String, dynamic> _pageMap = {};
  static LoadingScreen loadingScreen = LoadingScreen();
  static dynamic authenticationService;
  static Map<String, IconData> iconMap = {"access_time": Icons.access_time};
  static MaterialPageRoute Function(RouteSettings routeSettings) generateRoute;

  // static void setupPageMap(Map<String, dynamic> pageMap) {
  //   _pageMap = pageMap;
  // }

  // static Map<String, dynamic> siteConfigMap() {
  //   return _siteConfigMap;
  // }

  // static Map<String, dynamic> appNavigationMap() {
  //   return _appNavigationMap;
  // }

  // static Map<String, dynamic> pageMap() {
  //   return _pageMap;
  // }

  static void logout() {}
}
