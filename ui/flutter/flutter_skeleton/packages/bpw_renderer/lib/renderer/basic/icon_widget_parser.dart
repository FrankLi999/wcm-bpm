import 'package:flutter/material.dart';
import '../../bpw_renderer.dart';
import '../utils.dart';
import '../icons_helper.dart';

class IconWidgetParser extends WidgetParser {
  @override
  Widget parse(Map<String, dynamic> map, BuildContext buildContext,
      ClickListener listener) {
    return Icon(
      map.containsKey('data')
          ? getIconUsingPrefix(name: map['data'])
          : Icons.android,
      size: map.containsKey("size") ? map['size'] : null,
      color: map.containsKey('color') ? parseHexColor(map['color']) : null,
      semanticLabel:
          map.containsKey('semanticLabel') ? map['semanticLabel'] : null,
      textDirection: map.containsKey('textDirection')
          ? parseTextDirection(map['textDirection'])
          : null,
    );
  }

  @override
  String get widgetName => "Icon";
}
