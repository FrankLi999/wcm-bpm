class WcmUtils {
  static library(wcmPath) {
    return wcmPath.startsWith('/') ? wcmPath.split('/')[1] : wcmPath.split('/')[0];
  }
  static itemName(wcmPath) {
    const path = wcmPath.split('/');
    return path[path.length - 1];
  }
}
export default WcmUtils;
