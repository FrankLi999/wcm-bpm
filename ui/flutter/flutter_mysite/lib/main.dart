import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_i18n/flutter_i18n.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:bpw_common/theme/AppTheme.dart';
import 'package:bpw_common/app_context/AppContextNotifier.dart';
import 'package:bpw_common/app_context/AppContext.dart';
import 'package:bpw_common/nav/navigator/bloc/nav_bloc.dart';
import 'package:bpw_common/nav/navigator/bloc/nav_repository.dart';
import 'package:bpw_wcm/pages/root_page.dart';
import 'package:bpw_common/auth/bloc/repo/authentication_repository.dart';
import 'package:bpw_common/auth/bloc/repo/user_repository.dart';
import 'package:bpw_common/auth/bloc/auth/authentication_bloc.dart';
import 'package:bpw_common/auth/service/auth_service.dart';
import 'package:bpw_wcm/bloc/site_config/site_config_repository.dart';
import 'package:bpw_wcm/bloc/site_config/site_config_bloc.dart';
import './route/Routes.dart';

main() {
  WidgetsFlutterBinding.ensureInitialized();
  AppContext.generateRoute = Routes.generateRoute;
  AppContext.authenticationService = AuthenticationService();
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp])
      .then((_) async {
    final userRepository = UserRepository();
    final authenticationRepository = AuthenticationRepository();
    final NavRepository navRepository = NavRepository();
    final SiteConfigRepository siteConfigRepository = SiteConfigRepository();
    runApp(ChangeNotifierProvider<AppContextNotifier>(
        create: (context) {
          return AppContextNotifier(context);
        },
        child: MultiRepositoryProvider(
            providers: [
              RepositoryProvider.value(value: userRepository),
              RepositoryProvider.value(value: authenticationRepository),
              RepositoryProvider.value(value: navRepository),
              RepositoryProvider.value(value: siteConfigRepository)
            ],
            child: MultiBlocProvider(
              providers: [
                BlocProvider<AuthenticationBloc>(create: (context) {
                  return AuthenticationBloc(
                      authenticationRepository: authenticationRepository,
                      userRepository: userRepository)
                    ..add(AppStarted());
                }),
                BlocProvider<SiteConfigBloc>(create: (context) {
                  return SiteConfigBloc(
                      siteConfigRepository: siteConfigRepository,
                      userRepository: userRepository);
                }),
              ],
              child: MyApp(),
            ))));
  });
  //
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  ThemeData themeData;
  CustomAppTheme customAppTheme;
  NavBloc _navBloc;
  NavRepository _navRepository;
  @override
  void initState() {
    super.initState();
    _navRepository = RepositoryProvider.of<NavRepository>(context);
    _navBloc = NavBloc(navRepository: _navRepository);
  }

  final FlutterI18nDelegate flutterI18nDelegate = FlutterI18nDelegate(
    translationLoader: NamespaceFileTranslationLoader(
      namespaces: ["common", "home", "lang", "NAV", "navigation"],
      useCountryCode: false,
      fallbackDir: 'en',
      basePath: 'assets/i18n_namespace',
      forcedLocale: Locale('en'),
    ),
    missingTranslationHandler: (key, locale) {
      print("--- Missing Key: $key, languageCode: ${locale.languageCode}");
    },
  );
  @override
  Widget build(BuildContext context) {
    return Consumer<AppContextNotifier>(
      builder: (BuildContext context, AppContextNotifier value, Widget child) {
        themeData = AppTheme.getThemeFromThemeMode(value.themeMode());
        customAppTheme = AppTheme.getCustomAppTheme(value.themeMode());
        return BlocProvider<NavBloc>(
            create: (context) {
              return _navBloc;
            },
            child: MaterialApp(
                localizationsDelegates: [
                  flutterI18nDelegate,
                  GlobalMaterialLocalizations.delegate,
                  GlobalWidgetsLocalizations.delegate
                ],
                supportedLocales: [
                  const Locale('en', ''), // English, no country code
                  const Locale('fr', ''), // Hebrew, no country code
                  const Locale.fromSubtags(
                      languageCode:
                          'zh'), // Chinese *See Advanced Locales below*
                  // ... other locales the app supports
                ],
                builder: FlutterI18n.rootAppBuilder(),
                theme: AppTheme.getThemeFromThemeMode(value.themeMode()),
                debugShowCheckedModeBanner: false,
                home: BlocListener<AuthenticationBloc, AuthenticationState>(
                  listener: (context, state) {
                    if (state is AuthenticationUnintialized) {
                      // return SplashPage();
                    } else if (state is AuthenticationAuthenticated) {
                      BlocProvider.of<SiteConfigBloc>(context).add(
                          ProtectedSiteConfigEvent(
                              repository: 'bpwizard',
                              workspace: 'default',
                              library: 'mysite',
                              site: 'mysite'));
                      if (_navRepository.navPath != '/mysite/home') {
                        _navBloc.add(NavEvent(navPath: _nextPage()));
                        // Navigator.of(context).pushReplacementNamed(_nextPage());
                      }
                    } else if (state is AuthenticationUnauthenticated) {
                      // return LoginPage(userRepository: userRepository,);
                      BlocProvider.of<SiteConfigBloc>(context).add(
                          PublicSiteConfigEvent(
                              repository: 'bpwizard',
                              workspace: 'default',
                              library: 'mysite',
                              site: 'mysite'));
                      if (_navRepository.navPath != '/mysite/home') {
                        _navBloc.add(NavEvent(navPath: _nextPage()));
                        // Navigator.of(context).pushReplacementNamed(_nextPage());
                      }
                    } else if (state is AuthenticationLoading) {
                      // return LoadingIndicator();
                    }
                  },
                  child: RootPage(
                      themeData: themeData, customAppTheme: customAppTheme),
                ),
                onGenerateRoute: (routeSettings) =>
                    Routes.generateRoute(routeSettings)));
      },
    );
  }

  String _nextPage() {
    String nextPage = (_navRepository.arguments ?? {})['referer'] as String;
    return nextPage == null ? '/mysite/home' : nextPage;
  }
}
