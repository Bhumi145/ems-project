import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';

interface PerformanceData {
  month: string;
  efficiency: number;
  tasks: number;
}

@Component({
  selector: 'app-performance-graph',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="performance-container">
      <div class="header">
        <h2>Employee Performance Metrics</h2>
        <p class="subtitle">Monthly efficiency and task completion overview</p>
      </div>

      <div class="metrics-summary">
        <div class="metric-card">
          <span class="label">Average Efficiency</span>
          <span class="value">{{ averageEfficiency }}%</span>
        </div>
        <div class="metric-card">
          <span class="label">Total Tasks</span>
          <span class="value">{{ totalTasks }}</span>
        </div>
      </div>

      <div class="graph-container">
        <!-- Simple CSS Bar Chart -->
        <div class="chart">
          <div class="y-axis">
            <span>100%</span>
            <span>75%</span>
            <span>50%</span>
            <span>25%</span>
            <span>0%</span>
          </div>
          <div class="bars">
            <div *ngFor="let data of performanceData" class="bar-group">
              <div class="bar" [style.height.%]="data.efficiency" [attr.data-value]="data.efficiency + '%'"></div>
              <span class="x-label">{{ data.month }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .performance-container {
      padding: 2rem;
      background-color: #1a1a1a;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.05);
      max-width: 800px;
      margin: 0 auto;
      color: white;
    }
    .header { margin-bottom: 2rem; }
    .subtitle { color: #6c757d; }

    .metrics-summary {
      display: flex;
      gap: 2rem;
      margin-bottom: 2rem;
    }
    .metric-card {
      background: #2a2a2a;
      padding: 1rem;
      border-radius: 6px;
      flex: 1;
      text-align: center;
    }
    .metric-card .label { display: block; font-size: 0.875rem; color: #6c757d; }
    .metric-card .value { display: block; font-size: 1.5rem; font-weight: bold; color: #ffffff; }

    .graph-container {
      height: 300px;
      margin-top: 2rem;
      border: 1px solid #333;
      padding: 1rem;
      border-radius: 4px;
    }
    .chart {
      display: flex;
      height: 100%;
      width: 100%;
    }
    .y-axis {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      padding-right: 10px;
      color: #999;
      font-size: 0.75rem;
      border-right: 1px solid #333;
    }
    .bars {
      display: flex;
      flex: 1;
      justify-content: space-around;
      align-items: flex-end;
      padding-left: 1rem;
    }
    .bar-group {
      display: flex;
      flex-direction: column;
      align-items: center;
      height: 100%;
      justify-content: flex-end;
      width: 40px;
    }
    .bar {
      width: 100%;
      background-color: #3498db;
      border-radius: 4px 4px 0 0;
      transition: height 0.3s ease;
      position: relative;
    }
    .bar:hover { background-color: #2980b9; }
    .bar:hover::after {
      content: attr(data-value);
      position: absolute;
      top: -25px;
      left: 50%;
      transform: translateX(-50%);
      background: #333;
      color: #fff;
      padding: 2px 6px;
      border-radius: 3px;
      font-size: 12px;
    }
    .x-label {
      margin-top: 10px;
      font-size: 0.75rem;
      color: #6c757d;
    }
  `]
})
export class PerformanceGraphComponent implements OnChanges {
  @Input() monthlyData: any[] = [];

  performanceData: PerformanceData[] = [];
  averageEfficiency: number = 0;
  totalTasks: number = 0;

  constructor() {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['monthlyData']) {
      this.transformData();
      this.calculateMetrics();
    }
  }

  transformData(): void {
    this.performanceData = this.monthlyData.map(item => ({
      month: `${item.month}/${item.year}`,
      efficiency: Math.round(item.completionRate * 100),
      tasks: item.total
    }));
  }

  calculateMetrics(): void {
    if (this.performanceData.length === 0) return;

    this.totalTasks = this.performanceData.reduce((acc, curr) => acc + curr.tasks, 0);
    const totalEfficiency = this.performanceData.reduce((acc, curr) => acc + curr.efficiency, 0);
    this.averageEfficiency = Math.round(totalEfficiency / this.performanceData.length);
  }
}
