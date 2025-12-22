import { AppRole } from '../constants/app.constants';

export interface UserResponse {
  id: number;
  email: string;
  role: AppRole;
  active: boolean;
}
