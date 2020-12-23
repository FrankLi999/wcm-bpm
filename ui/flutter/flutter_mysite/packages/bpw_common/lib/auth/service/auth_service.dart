import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import '../model/user_profile.dart';
import '../bloc/auth/authentication_bloc.dart';

class AuthenticationService {
  AuthenticationService() {}

  Future<User> login({
    @required String username,
    @required String password,
  }) async {
    UserLogin userLogin = UserLogin(email: username, password: password);
    const String apiUrl = "http://192.168.0.104:28080/core/api/login";
    final http.Response response = await http.post(
      apiUrl,
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(userLogin.toJson()),
    );
    if (response.statusCode == 200) {
      User user = User.fromJson(json.decode(response.body));
      return user;
    } else {
      throw Exception(json.decode(response.body));
    }
  }

  void logout(BuildContext context) {
    BlocProvider.of<AuthenticationBloc>(context).add(LoggedOut());
  }
}
