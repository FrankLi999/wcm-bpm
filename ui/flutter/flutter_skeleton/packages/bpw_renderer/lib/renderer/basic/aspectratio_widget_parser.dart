import 'package:flutter/widgets.dart';
import '../../bpw_renderer.dart';

class AspectRatioWidgetParser extends WidgetParser {
  @override
  Widget parse(Map<String, dynamic> map, BuildContext buildContext,
      ClickListener listener) {
    return AspectRatio(
      aspectRatio: map["aspectRatio"],
      child: DynamicWidgetBuilder.buildFromMap(
          map["child"], buildContext, listener),
    );
  }

  @override
  String get widgetName => "AspectRatio";
}
