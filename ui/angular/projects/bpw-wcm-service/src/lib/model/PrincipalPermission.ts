export interface PrincipalPermission {
  principalName: string;
  principalType: string;
  viewer: boolean;
  editor: boolean;
  administrator: boolean;
}
