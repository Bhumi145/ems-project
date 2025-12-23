import { AppRole } from '../constants/app.constants';

export enum TaskStatus {
  OPEN = 'OPEN',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  DRAFT = 'DRAFT',
  REVIEW = 'REVIEW',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  REWORK = 'REWORK'
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
  escalationLevel?: number;
  slaBreached?: boolean;
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
  [TaskStatus.CANCELLED]: { label: 'Cancelled', classes: 'bg-neutral/10 text-neutral border border-neutral/30' },
  [TaskStatus.DRAFT]: { label: 'Draft', classes: 'bg-gray-500/10 text-gray-100 border border-gray-400/30' },
  [TaskStatus.REVIEW]: { label: 'Under Review', classes: 'bg-blue-500/10 text-blue-100 border border-blue-400/30' },
  [TaskStatus.APPROVED]: { label: 'Approved', classes: 'bg-green-500/10 text-green-100 border border-green-400/30' },
  [TaskStatus.REJECTED]: { label: 'Rejected', classes: 'bg-red-500/10 text-red-100 border border-red-400/30' },
  [TaskStatus.REWORK]: { label: 'Rework', classes: 'bg-orange-500/10 text-orange-100 border border-orange-400/30' }
};

export const PRIORITY_META: Record<TaskPriority, { label: string; classes: string }> = {
  [TaskPriority.LOW]: { label: 'Low', classes: 'text-positive border border-positive/30 bg-positive/10' },
  [TaskPriority.MEDIUM]: { label: 'Medium', classes: 'text-accent border border-accent/30 bg-accent/10' },
  [TaskPriority.HIGH]: { label: 'High', classes: 'text-warning border border-warning/30 bg-warning/10' },
  [TaskPriority.CRITICAL]: { label: 'Critical', classes: 'text-danger border border-danger/40 bg-danger/10' }
};