class NavRepository {
  String _navPath;
  Map<String, dynamic> _arguments;
  String get navPath => _navPath;
  Map<String, dynamic> get arguments => _arguments;

  void setNavPath(String navPath, [Map<String, dynamic> arguments]) {
    _navPath = navPath;
    _arguments = arguments;
  }

  void resetNavigation(String navPath) {
    _navPath = navPath;
  }
}
