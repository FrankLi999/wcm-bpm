import "nested_nav_item.dart";

class NavUtils {
  static NestedNavigatorItem findNestedNavItem(
      String currentKey, List<NestedNavigatorItem> items) {
    if (currentKey == null) {
      return null;
    }
    for (var i = 0; i < items.length; i++) {
      if (items[i].url == currentKey) {
        return items[i];
      }
      if (items[i].children != null) {
        NestedNavigatorItem navItem =
            findNestedNavItem(currentKey, items[i].children);
        if (navItem != null) {
          return navItem;
        }
      }
    }
    return null;
  }
}
