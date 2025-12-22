import { AppRole } from '../constants/app.constants';

export interface EmployeeResponse {
	id: number;
	userId: number;
	fullName: string;
	department?: string;
	title?: string;
	role: AppRole;
	managerId?: number;
	active: boolean;
}

export interface CreateEmployeeRequest {
	fullName: string;
	email: string;
	password: string;
	department?: string;
	title?: string;
	role: AppRole;
	managerId?: number | null;
}
