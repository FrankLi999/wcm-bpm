part of 'site_config_bloc.dart';

abstract class SiteConfigEvent extends Equatable {
  const SiteConfigEvent();
}

class ProtectedSiteConfigEvent extends SiteConfigEvent {
  final String repository;
  final String workspace;
  final String library;
  final String site;
  const ProtectedSiteConfigEvent(
      {@required this.repository,
      @required this.workspace,
      @required this.library,
      @required this.site});

  @override
  List<Object> get props => [repository, workspace, library, site];

  @override
  String toString() => 'Site Config:{$repository/$workspace/$library/$site}';
}

class PublicSiteConfigEvent extends SiteConfigEvent {
  final String repository;
  final String workspace;
  final String library;
  final String site;
  const PublicSiteConfigEvent(
      {@required this.repository,
      @required this.workspace,
      @required this.library,
      @required this.site});

  @override
  List<Object> get props => [repository, workspace, library, site];

  @override
  String toString() => 'Site Config:{$repository/$workspace/$library/$site}';
}
