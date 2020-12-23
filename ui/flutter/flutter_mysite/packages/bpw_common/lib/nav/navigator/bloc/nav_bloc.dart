import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:meta/meta.dart';
import 'package:equatable/equatable.dart';

import 'nav_repository.dart';

part 'nav_event.dart';
part 'nav_state.dart';

class NavBloc extends Bloc<NavEvent, NavState> {
  final NavRepository navRepository;

  NavBloc({
    @required this.navRepository,
  })  : assert(navRepository != null),
        super(NavState(navPath: "/mysite/home"));

  @override
  Stream<NavState> mapEventToState(
    NavEvent event,
  ) async* {
    navRepository.setNavPath(event.navPath, event.arguments);
    yield NavState(navPath: event.navPath);
  }
}
