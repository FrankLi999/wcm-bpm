export interface UserProfile {
    id: string;
    email: string;
    firstName?: string;
    lastName?: string;
    name?: string;
    imageUrl?: string;
    accessToken: string;
    roles: string[];
    tokenType: string;
}