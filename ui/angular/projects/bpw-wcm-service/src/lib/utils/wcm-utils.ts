import { WcmConstants } from "./wcm-constants";

export class WcmUtils {
  static contentAreaLayoutPath(library: string, name: string): string {
    return `/${library}/${WcmConstants.ROOTNODE_LAYOUT}/${name}`;
  }

  static itemPath(library: string, rootNode: string, name: string): string {
    return `/${library}/${rootNode}/${name}`;
  }

  static library(wcmPath: string): string {
    return wcmPath.startsWith("/")
      ? wcmPath.split("/")[1]
      : wcmPath.split("/")[0];
  }
  static itemName(wcmPath: string) {
    const path: string[] = wcmPath.split("/");
    return path[path.length - 1];
  }

  static rootPath(wcmPath: string, library: string, rootNode: string): string {
    return wcmPath.substring(`/${library}/${rootNode}/`.length);
  }

  static parentPath(wcmPath: string): string {
    return wcmPath.split("/").length === 3
      ? null
      : wcmPath.substring(0, wcmPath.lastIndexOf("/"));
  }

  static isViewer(roles: string[]): boolean {
    return roles.some(
      (role) =>
        role === WcmConstants.ROLE_VIEWER ||
        role === WcmConstants.ROLE_EDITOR ||
        role === WcmConstants.ROLE_ADMIN
    );
  }

  static isEditor(roles: string[]): boolean {
    return roles.some(
      (role) =>
        role === WcmConstants.ROLE_EDITOR || role === WcmConstants.ROLE_ADMIN
    );
  }

  static isAdministrator(roles: string[]): boolean {
    return roles.some((role) => role === WcmConstants.ROLE_ADMIN);
  }

  //message
  //acl.component - 96, 116
}
