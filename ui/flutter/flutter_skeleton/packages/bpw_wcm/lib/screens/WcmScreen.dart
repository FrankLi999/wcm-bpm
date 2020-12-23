import 'package:flutter/material.dart';

class WcmScreen extends StatelessWidget {
  final String wcmPath;
  // ThemeData themeData;
  // CustomAppTheme customAppTheme;
  // WcmScreen({Key key, ThemeData themeData, CustomAppTheme customAppTheme})
  //     : super(key: key) {
  //   this.themeData = themeData;
  //   this.customAppTheme = customAppTheme;
  // }
  WcmScreen({Key key, @required this.wcmPath}) : super(key: key);
  // final GlobalKey<ScaffoldState> _scaffoldKey = new GlobalKey<ScaffoldState>();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          wcmPath,
        ),
        leading: GestureDetector(
          onTap: () {
            Scaffold.of(context).openDrawer();
          },
          child: Icon(
            Icons.menu, // add custom icons also
          ),
        ),
        // backgroundColor: Colors.blue,
      ),
      body: Center(
        child: Text(wcmPath),
      ),
    );
  }
}
