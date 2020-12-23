// library bpw_renderer;
import 'package:flutter/widgets.dart';
import 'package:logging/logging.dart';
import 'dart:convert';
import './renderer/basic/align_widget_parser.dart';
import './renderer/basic/aspectratio_widget_parser.dart';
import './renderer/basic/baseline_widget_parser.dart';
import './renderer/basic/button_widget_parser.dart';
import './renderer/basic/center_widget_parser.dart';
import './renderer/basic/container_widget_parser.dart';
import './renderer/basic/dropcaptext_widget_parser.dart';
import './renderer/basic/expanded_widget_parser.dart';
import './renderer/basic/fittedbox_widget_parser.dart';
import './renderer/basic/icon_widget_parser.dart';
import './renderer/basic/image_widget_parser.dart';
import './renderer/basic/indexedstack_widget_parser.dart';
import './renderer/basic/listtile_widget_parser.dart';
import './renderer/basic/opacity_widget_parser.dart';
import './renderer/basic/padding_widget_parser.dart';
import './renderer/basic/placeholder_widget_parser.dart';
import './renderer/basic/row_column_widget_parser.dart';
import './renderer/basic/safearea_widget_parser.dart';
import './renderer/basic/selectabletext_widget_parser.dart';
import './renderer/basic/sizedbox_widget_parser.dart';
import './renderer/basic/stack_positioned_widgets_parser.dart';
import './renderer/basic/text_widget_parser.dart';
import './renderer/basic/wrap_widget_parser.dart';
import './renderer/scrolling/gridview_widget_parser.dart';
import './renderer/scrolling/listview_widget_parser.dart';
import './renderer/scrolling/pageview_widget_parser.dart';
import './renderer/basic/cliprrect_widget_parser.dart';

class DynamicWidgetBuilder {
  static final Logger log = Logger('DynamicWidget');

  static final _parsers = [
    ContainerWidgetParser(),
    TextWidgetParser(),
    SelectableTextWidgetParser(),
    RaisedButtonParser(),
    RowWidgetParser(),
    ColumnWidgetParser(),
    AssetImageWidgetParser(),
    NetworkImageWidgetParser(),
    PlaceholderWidgetParser(),
    GridViewWidgetParser(),
    ListViewWidgetParser(),
    PageViewWidgetParser(),
    ExpandedWidgetParser(),
    PaddingWidgetParser(),
    CenterWidgetParser(),
    AlignWidgetParser(),
    AspectRatioWidgetParser(),
    FittedBoxWidgetParser(),
    BaselineWidgetParser(),
    StackWidgetParser(),
    PositionedWidgetParser(),
    IndexedStackWidgetParser(),
    ExpandedSizedBoxWidgetParser(),
    SizedBoxWidgetParser(),
    OpacityWidgetParser(),
    WrapWidgetParser(),
    DropCapTextParser(),
    IconWidgetParser(),
    ClipRRectWidgetParser(),
    SafeAreaWidgetParser(),
    ListTileWidgetParser()
  ];

  static final _widgetNameParserMap = <String, WidgetParser>{};

  static bool _defaultParserInited = false;

  // use this method for adding your custom widget parser
  static void addParser(WidgetParser parser) {
    log.info(
        "add custom widget parser, make sure you don't overwirte the widget type.");
    _parsers.add(parser);
    _widgetNameParserMap[parser.widgetName] = parser;
  }

  static void initDefaultParsersIfNess() {
    if (!_defaultParserInited) {
      for (var parser in _parsers) {
        _widgetNameParserMap[parser.widgetName] = parser;
      }
      _defaultParserInited = true;
    }
  }

  static Widget build(
      String json, BuildContext buildContext, ClickListener listener) {
    initDefaultParsersIfNess();
    var map = jsonDecode(json);
    ClickListener _listener =
        listener == null ? new NonResponseWidgetClickListener() : listener;
    var widget = buildFromMap(map, buildContext, _listener);
    return widget;
  }

  static Widget buildFromMap(Map<String, dynamic> map,
      BuildContext buildContext, ClickListener listener) {
    String widgetName = map['type'];
    var parser = _widgetNameParserMap[widgetName];
    if (parser != null) {
      return parser.parse(map, buildContext, listener);
    }
    log.warning("Not support type: $widgetName");
    return null;
  }

  static List<Widget> buildWidgets(
      List<dynamic> values, BuildContext buildContext, ClickListener listener) {
    List<Widget> rt = [];
    for (var value in values) {
      rt.add(buildFromMap(value, buildContext, listener));
    }
    return rt;
  }
}

/// extends this class to make a Flutter widget parser.
abstract class WidgetParser {
  /// parse the json map into a flutter widget.
  Widget parse(Map<String, dynamic> map, BuildContext buildContext,
      ClickListener listener);

  /// the widget type name for example:
  /// {"type" : "Text", "data" : "Denny"}
  /// if you want to make a flutter Text widget, you should implement this
  /// method return "Text", for more details, please see
  /// @TextWidgetParser
  String get widgetName;
}

abstract class ClickListener {
  void onClicked(String event);
}

class NonResponseWidgetClickListener implements ClickListener {
  static final Logger log = Logger('NonResponseWidgetClickListener');

  @override
  void onClicked(String event) {
    log.info("receiver click event: " + event);
    print("receiver click event: " + event);
  }
}
