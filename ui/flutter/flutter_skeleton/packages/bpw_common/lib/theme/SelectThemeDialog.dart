import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'AppTheme.dart';
import '../app_context/AppContextNotifier.dart';
import '../utils/SizeConfig.dart';

class SelectThemeDialog extends StatefulWidget {
  @override
  _SelectThemeDialogState createState() => _SelectThemeDialogState();
}

class _SelectThemeDialogState extends State<SelectThemeDialog> {
  ThemeData themeData;

  void _handleRadioValueChange(int value) {
    Navigator.of(context).pop();
    setState(() {
      Provider.of<AppContextNotifier>(context, listen: false).updateTheme(value);
    });
  }

  Widget build(BuildContext context) {
    themeData = Theme.of(context);
    return Consumer<AppContextNotifier>(
      builder: (BuildContext context, AppContextNotifier value, Widget child) {
        return Dialog(
          child: Container(
            padding: EdgeInsets.only(top: MySize.size16, bottom: MySize.size16),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: <Widget>[
                InkWell(
                  onTap: () {
                    _handleRadioValueChange(AppTheme.themeLight);
                  },
                  child: Container(
                    padding: EdgeInsets.only(
                        left: MySize.size16, right: MySize.size16),
                    child: Row(
                      children: <Widget>[
                        Radio(
                          onChanged: (value) {
                            _handleRadioValueChange(value);
                          },
                          groupValue: value.themeMode(),
                          value: AppTheme.themeLight,
                          activeColor: themeData.colorScheme.primary,
                        ),
                        Text("Light Theme",
                            style: AppTheme.getTextStyle(
                                themeData.textTheme.subtitle2,
                                fontWeight: 600)),
                        Container(
                          margin: EdgeInsets.only(left: 16),
                          width: MySize.size22,
                          height: MySize.size22,
                          decoration: BoxDecoration(
                              border: Border.all(
                                  color: themeData.colorScheme.onBackground,
                                  width: 1),
                              color: AppTheme.lightTheme.backgroundColor,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(11))),
                        ),
                        Container(
                          margin: EdgeInsets.only(left: 8),
                          width: MySize.size22,
                          height: MySize.size22,
                          decoration: BoxDecoration(
                              border: Border.all(
                                  color: themeData.colorScheme.onBackground,
                                  width: 1),
                              color: AppTheme.lightTheme.colorScheme.primary,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(11))),
                        ),
                        Container(
                          margin: EdgeInsets.only(left: 8),
                          width: MySize.size22,
                          height: MySize.size22,
                          decoration: BoxDecoration(
                              border: Border.all(
                                  color: themeData.colorScheme.onBackground,
                                  width: 1),
                              color: AppTheme.lightTheme.colorScheme.secondary,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(11))),
                        ),
                      ],
                    ),
                  ),
                ),
                InkWell(
                  onTap: () {
                    _handleRadioValueChange(AppTheme.themeDark);
                  },
                  child: Container(
                    padding: EdgeInsets.only(
                        left: MySize.size16, right: MySize.size16),
                    child: Row(
                      children: <Widget>[
                        Radio(
                          onChanged: (value) {
                            _handleRadioValueChange(value);
                          },
                          groupValue: value.themeMode(),
                          value: AppTheme.themeDark,
                          activeColor: themeData.colorScheme.secondary,
                        ),
                        Text("Dark Theme ",
                            style: AppTheme.getTextStyle(
                                themeData.textTheme.subtitle2,
                                fontWeight: 600)),
                        Container(
                          margin: EdgeInsets.only(left: MySize.size16),
                          width: MySize.size22,
                          height: MySize.size22,
                          decoration: BoxDecoration(
                              border: Border.all(
                                  color: themeData.colorScheme.onBackground,
                                  width: 1),
                              color: AppTheme.darkTheme.backgroundColor,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(11))),
                        ),
                        Container(
                          margin: EdgeInsets.only(left: MySize.size8),
                          width: MySize.size22,
                          height: MySize.size22,
                          decoration: BoxDecoration(
                              border: Border.all(
                                  color: themeData.colorScheme.onBackground,
                                  width: 1),
                              color: AppTheme.darkTheme.colorScheme.primary,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(11))),
                        ),
                        Container(
                          margin: EdgeInsets.only(left: MySize.size8),
                          width: MySize.size22,
                          height: MySize.size22,
                          decoration: BoxDecoration(
                              border: Border.all(
                                  color: themeData.colorScheme.onBackground,
                                  width: 1),
                              color: AppTheme.darkTheme.colorScheme.secondary,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(11))),
                        ),
                      ],
                    ),
                  ),
                ),
                Container(
                  margin: EdgeInsets.only(top: MySize.size8),
                  child: Text(
                    "More themes are coming soon...",
                    style: AppTheme.getTextStyle(themeData.textTheme.bodyText2,
                        fontWeight: 600),
                  ),
                )
              ],
            ),
          ),
        );
      },
    );
  }
}
