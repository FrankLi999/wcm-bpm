import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { map, tap } from "rxjs/operators";
import { forkJoin } from "rxjs";
import { TaskService } from "../../../../services/task.service";

@Component({
  // selector: "camunda-human-tasks",
  templateUrl: "./human-tasks.component.html",
  styleUrls: ["./human-tasks.component.scss"],
})
export class HumanTasksComponent implements OnInit {
  total: number;
  widget: any = {
    scheme: {
      //domain: ["#4867d2", "#5c84f1", "#89a9f4"],
      domain: ["#5AA454", "#A10A28", "#C7B42C", "#AAAAAA"],
    },
    values: [],
  };
  constructor(private _router: Router, private _taskService: TaskService) {}

  ngOnInit(): void {
    forkJoin(
      // as of RxJS 6.5+ we can use a dictionary of sources
      {
        total: this._taskService
          .getTasksCount()
          .pipe(map((result) => result.count)),
        assigned: this._taskService
          .getAssigned(true, true)
          .pipe(map((result) => result.count)),
        unassignedWithCandidateGroup: this._taskService
          .getUnassignedWithCandidateGroup(true, true, true)
          .pipe(map((result) => result.count)),
        unassignedWithoutCandidateGroup: this._taskService
          .getUnassignedWithoutCandidateGroup(true, true, true)
          .pipe(map((result) => result.count)),
      }
    )
      .pipe(tap((result) => this._processResult(result)))
      .subscribe();
  }

  checkTasks() {
    this._router.navigateByUrl("/bpm/dashboard/tasks");
  }
  onSelect(data): void {
    console.log("Item clicked", JSON.parse(JSON.stringify(data)));
    if (data.name !== "No data") {
      this._router.navigateByUrl("/bpm/dashboard/tasks");
    }
  }

  tooltipText(data) {
    return data.data.name === "No data"
      ? "No Data"
      : `${data.data.name}: ${data.data.value}`;
  }

  private _processResult(result) {
    this.total = result.total;
    if (result.assigned) {
      this.widget.values.push({
        name: "Assigned",
        value: result.assigned,
      });
    }
    if (result.unassignedWithCandidateGroup) {
      this.widget.values.push({
        name: "Unassigned with canadidate group",
        value: result.unassignedWithCandidateGroup,
      });
    }
    if (result.unassignedWithoutCandidateGroup) {
      this.widget.values.push({
        name: "Unassigned without canadidate group",
        value: result.unassignedWithoutCandidateGroup,
      });
    }

    if (this.widget.values.length == 0) {
      this.widget.values.push({
        name: "No data",
        legendTitle: "No data 10",
        legend: true,
        value: 1,
      });
    }
  }
}
