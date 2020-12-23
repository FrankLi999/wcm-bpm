class SiteConfigRepository {
  String repository;
  String workspace;
  String site;
  String library;
  dynamic siteConfig;
  Map<String, dynamic> siteConfigMap = {};
  Map<String, dynamic> appNavigationMap = {};
  SiteConfigRepository();

  void setSiteConfig(String repository, String workspace, String librar,
      String site, Map<String, dynamic> siteConfigMap) {
    this.repository = repository;
    this.workspace = workspace;
    this.library = library;
    this.site = site;
    this.siteConfigMap = siteConfigMap;
  }

  void setAppNavigation(Map<String, dynamic> appNavigationMap) {
    this.appNavigationMap = appNavigationMap;
  }

  void resetSiteConfig() {
    siteConfig = null;
  }
}
