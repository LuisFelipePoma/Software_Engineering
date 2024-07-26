export interface AuthRequest {
    email?: string;
    password?: string;
}

export interface AuthResponse {
    token: string;
    user: Profile
}

export interface Profile {
    firstName: string;
    lastName: string;
    fullName: string;
    email: string;
    role: 'USER' | 'ADMIN';
}

export interface SignupRequest {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
}