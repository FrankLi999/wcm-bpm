import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:meta/meta.dart';
import 'package:equatable/equatable.dart';
import 'package:bpw_common/auth/bloc/repo/user_repository.dart';
import 'site_config_repository.dart';
import '../../services/site_config_service.dart';
part 'site_config_event.dart';
part 'site_config_state.dart';

class SiteConfigBloc extends Bloc<SiteConfigEvent, SiteConfigState> {
  final SiteConfigRepository siteConfigRepository;
  final UserRepository userRepository;
  final SiteConfigService siteConfigService = SiteConfigService();
  SiteConfigBloc({
    @required this.siteConfigRepository,
    @required this.userRepository,
  })  : assert(siteConfigRepository != null),
        super(SiteConfigInitial());

  @override
  Stream<SiteConfigState> mapEventToState(
    SiteConfigEvent event,
  ) async* {
    if (event is ProtectedSiteConfigEvent) {
      yield SiteConfigLoading();
      try {
        Map<String, dynamic> siteConfig =
            await siteConfigService.getProtectedSiteConfig(
                event.repository,
                event.workspace,
                event.library,
                event.site,
                userRepository.getToken());
        siteConfigRepository.setSiteConfig(event.repository, event.workspace,
            event.library, event.site, siteConfig);
        yield ProtectedSiteConfigUpdated();
        // yield SiteConfigInitial();
      } catch (error) {
        yield SiteConfigFaliure(error: error.toString());
      }
    }

    if (event is PublicSiteConfigEvent) {
      yield SiteConfigLoading();
      Map<String, dynamic> appNavigationMap =
          await siteConfigService.loadAppNavigation();
      siteConfigRepository.setAppNavigation(appNavigationMap);
      Map<String, dynamic> siteConfig =
          await siteConfigService.getPublicSiteConfig(
              event.repository, event.workspace, event.library, event.site);
      siteConfigRepository.setSiteConfig(event.repository, event.workspace,
          event.library, event.site, siteConfig);
      yield PublicSiteConfigUpdated();
      // yield SiteConfigInitial();
    }
  }
}
