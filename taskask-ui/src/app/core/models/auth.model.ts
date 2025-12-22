import { AppRole } from '../constants/app.constants';

export interface AuthResponse {
  token: string;
  email: string;
  role: AppRole;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  fullName: string;
  role: AppRole;
}

export interface AuthSession extends AuthResponse {}
