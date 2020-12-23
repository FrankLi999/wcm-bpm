import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:meta/meta.dart';
import 'package:equatable/equatable.dart';

import '../repo/user_repository.dart';
import '../repo/authentication_repository.dart';
import '../../model/user_profile.dart';

part 'authentication_event.dart';
part 'authentication_state.dart';

class AuthenticationBloc
    extends Bloc<AuthenticationEvent, AuthenticationState> {
  final UserRepository userRepository;
  final AuthenticationRepository authenticationRepository;

  AuthenticationBloc({
    @required this.authenticationRepository,
    @required this.userRepository,
  })  : assert(authenticationRepository != null),
        assert(userRepository != null),
        super(AuthenticationUnintialized());

  @override
  Stream<AuthenticationState> mapEventToState(
    AuthenticationEvent event,
  ) async* {
    if (event is AppStarted) {
      // final bool hasToken = await userRepository.hasToken();
      final bool hasToken = userRepository.hasToken();
      if (hasToken) {
        yield AuthenticationAuthenticated();
      } else {
        yield AuthenticationUnauthenticated();
      }
    }

    if (event is LoggedIn) {
      yield AuthenticationLoading();

      // await userRepository.setUser(user: event.user);
      userRepository.setUser(event.user);
      yield AuthenticationAuthenticated();
    }

    if (event is LoggedOut) {
      yield AuthenticationLoading();

      // await userRepository.deleteUser();
      userRepository.deleteUser();
      yield AuthenticationUnauthenticated();
    }
  }
}
