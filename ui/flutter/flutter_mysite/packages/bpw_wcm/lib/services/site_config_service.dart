import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter/services.dart';

class SiteConfigService {
  SiteConfigService();

  Future<Map<String, dynamic>> getProtectedSiteConfig(
    String repository,
    String workspace,
    String library,
    String site,
    String token,
  ) async {
    String jsonString =
        await rootBundle.loadString('assets/sites/$site' + '_protected.json');
    return json.decode(jsonString);

    // String apiUrl =
    //     "http://192.168.0.104:28080/wcm/api/wcmSystem/$repository/$workspace/$library/$site?authoring=false";
    // final http.Response response =
    //     await http.get(apiUrl, headers: <String, String>{
    //   'Content-Type': 'application/json; charset=UTF-8',
    //   'Authorization': 'Bearer $token',
    // });
    // if (response.statusCode == 200) {
    //   Map<String, dynamic> siteConfig = json.decode(response.body);
    //   return siteConfig;
    // } else {
    //   throw Exception(json.decode(response.body));
    // }
  }

  Future<Map<String, dynamic>> loadAppNavigation() async {
    String jsonString =
        await rootBundle.loadString('assets/sites/app_navigation.json');
    return json.decode(jsonString);
  }

  Future<Map<String, dynamic>> getPublicSiteConfig(
      String repository, String workspace, String library, String site) async {
    // http://localhost:28080/wcm/api/wcmSystem/bpwizard/default/mysite/mysite?authoring=false
    String jsonString =
        await rootBundle.loadString('assets/sites/$site' + '_public.json');
    return json.decode(jsonString);
  }
}
