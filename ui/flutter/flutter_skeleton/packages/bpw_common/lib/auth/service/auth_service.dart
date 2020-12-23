import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'dart:async';
import 'dart:convert';
import '../model/user_profile.dart';
import '../bloc/auth/authentication_bloc.dart';

class AuthenticationService {
  // final _userController = ReplaySubject<User>(maxSize: 1);

  AuthenticationService() {
    // Future.delayed(Duration(seconds: 10))
    //     .then((value) => _userController.add(null)); //Simulate loading
  }

  User login({
    @required String username,
    @required String password,
  }) {
    UserLogin userLogin = UserLogin(email: username, password: password);
    User user = User();
    user.id = "id";
    user.name = username;
    user.firstName = "User";
    user.lastName = "Flutter";
    user.email = username;
    user.accessToken = "token";
    user.expireIn = -1;
    user.roles = ["admin"];
    user.tokenType = "bearer";

    return user;
  }

  void logout(BuildContext context) {
    BlocProvider.of<AuthenticationBloc>(context).add(LoggedOut());
  }
}