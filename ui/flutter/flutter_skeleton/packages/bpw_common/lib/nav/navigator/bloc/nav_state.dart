part of 'nav_bloc.dart';

class NavState extends Equatable {
  final String navPath;
  @override
  List<Object> get props => [navPath];
  NavState({@required this.navPath}) : assert(navPath != null);
}
