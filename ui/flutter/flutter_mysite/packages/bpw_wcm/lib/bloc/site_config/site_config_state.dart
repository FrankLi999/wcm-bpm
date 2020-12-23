part of 'site_config_bloc.dart';

abstract class SiteConfigState extends Equatable {
  @override
  List<Object> get props => [];
  const SiteConfigState();
}

class SiteConfigInitial extends SiteConfigState {}

class SiteConfigLoading extends SiteConfigState {}

class PublicSiteConfigUpdated extends SiteConfigState {}

class ProtectedSiteConfigUpdated extends SiteConfigState {}

class SiteConfigFaliure extends SiteConfigState {
  final String error;

  const SiteConfigFaliure({@required this.error});

  @override
  List<Object> get props => [error];

  @override
  String toString() => ' SiteConfigFaliure { error: $error }';
}
