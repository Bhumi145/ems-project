import { AppRole } from '../constants/app.constants';

export enum TaskStatus {
  OPEN = 'OPEN',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  BLOCKED = 'BLOCKED'
}

export enum TaskPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL'
}

export interface TaskResponse {
  id: number;
  title: string;
  description: string;
  priority: TaskPriority;
  status: TaskStatus;
  startDate?: string;
  dueDate?: string;
  assigneeUsername?: string;
  assigneeNames?: string[];
  createdAt: string;
  updatedAt: string;
}

export interface TaskDashboardResponse {
  open: number;
  inProgress: number;
  completed: number;
  blocked: number;
}

export interface CreateTaskRequest {
  title: string;
  description?: string;
  priority: TaskPriority;
  startDate?: string | null;
  dueDate?: string | null;
  assigneeIds: number[];
}

export interface UpdateTaskStatusRequest {
  status: TaskStatus;
}

export interface TaskCommentResponse {
  id: number;
  authorEmail: string;
  comment: string;
  createdAt: string;
}

export interface TaskCommentRequest {
  comment: string;
}

export const STATUS_META: Record<TaskStatus, { label: string; classes: string }> = {
  [TaskStatus.OPEN]: { label: 'Open', classes: 'bg-base-500/10 text-base-100 border border-base-400/30' },
  [TaskStatus.IN_PROGRESS]: { label: 'In Progress', classes: 'bg-accent/15 text-accent border border-accent/30' },
  [TaskStatus.COMPLETED]: { label: 'Completed', classes: 'bg-positive/20 text-positive border border-positive/20' },
  [TaskStatus.BLOCKED]: { label: 'Blocked', classes: 'bg-danger/10 text-danger border border-danger/30' }
};

export const PRIORITY_META: Record<TaskPriority, { label: string; classes: string }> = {
  [TaskPriority.LOW]: { label: 'Low', classes: 'text-positive border border-positive/30 bg-positive/10' },
  [TaskPriority.MEDIUM]: { label: 'Medium', classes: 'text-accent border border-accent/30 bg-accent/10' },
  [TaskPriority.HIGH]: { label: 'High', classes: 'text-warning border border-warning/30 bg-warning/10' },
  [TaskPriority.CRITICAL]: { label: 'Critical', classes: 'text-danger border border-danger/40 bg-danger/10' }
};