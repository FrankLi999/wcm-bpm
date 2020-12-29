export interface UserProfile {
  id: string;
  email: string;
  firstName?: string;
  lastName?: string;
  name?: string;
  imageUrl?: string;
  accessToken: string;
  expireIn: number;
  roles: string[];
  tokenType: string;
}
