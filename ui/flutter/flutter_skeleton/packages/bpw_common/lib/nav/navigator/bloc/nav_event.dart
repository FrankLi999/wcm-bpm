part of 'nav_bloc.dart';

class NavEvent extends Equatable {
  final String navPath;
  final Map<String, dynamic> arguments;
  const NavEvent({@required this.navPath, this.arguments});

  @override
  List<Object> get props => [navPath, arguments];

  @override
  String toString() => 'Navigation Path:{$navPath}';
}
