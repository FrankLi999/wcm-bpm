import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import './AppContext.dart';
class AppContextNotifier extends ChangeNotifier {

  int _themeMode = 1;
  AppContext _appContext = AppContext();
  AppContextNotifier(BuildContext buildContext) {
    init();
  }

  init() async {
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    int data =  sharedPreferences.getInt("themeMode");
    if(data == null) {
      _themeMode = 1;
    } else {
      _themeMode = data;
    }
    notifyListeners();
  }

  themeMode() => _themeMode;
  appContext() => _appContext;

  notify(){
    notifyListeners();
  }

  Future<void> updateTheme(int themeMode) async {
    this._themeMode = themeMode;
    notifyListeners();
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    sharedPreferences.setInt("themeMode", themeMode);
  }
}