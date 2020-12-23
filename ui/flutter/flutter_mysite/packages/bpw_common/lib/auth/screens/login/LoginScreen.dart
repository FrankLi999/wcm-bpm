import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import '../../bloc/repo/user_repository.dart';
import '../../bloc/auth/authentication_bloc.dart';
import '../../bloc/login/login_bloc.dart';
import '../../../theme/AppTheme.dart';
import '../../../app_context/AppContextNotifier.dart';
import 'LoginForm.dart';

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  CustomAppTheme customAppTheme;
  ThemeData themeData;

  @override
  Widget build(BuildContext context) {
    themeData = Theme.of(context);
    return Consumer<AppContextNotifier>(builder:
        (BuildContext context, AppContextNotifier value, Widget child) {
      customAppTheme = AppTheme.getCustomAppTheme(value.themeMode());
      // return LoginForm();
      return BlocProvider(
        create: (context) {
          return LoginBloc(
            authenticationBloc: BlocProvider.of<AuthenticationBloc>(context),
            userRepository: RepositoryProvider.of<UserRepository>(context),
          );
        },
        child: LoginForm(),
      );
    });
  }
}
