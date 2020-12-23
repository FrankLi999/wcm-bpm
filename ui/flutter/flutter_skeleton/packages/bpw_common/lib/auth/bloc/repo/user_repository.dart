// import 'dart:async';
import '../../model/user_profile.dart';
import 'package:bpw_common/app_context/AppContext.dart';

class UserRepository {
  User _user;

  User getUser() {
    return _user;
  }

  void setUser(User user) {
    _user = user;
  }

  String getToken() {
    return (_user == null) ? null : _user.accessToken;
  }

  void deleteUser() {
    _user = null;
  }

  bool hasToken() {
    return _user != null && _user.accessToken != null;
  }

  Future<User> authenticate({String username, String password}) async {
    User user = await AppContext.authenticationService
        .login(username: username, password: password);
    return user;
  }
}
