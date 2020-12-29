import {
  Component,
  OnInit,
  ViewChild,
  ViewEncapsulation,
  OnDestroy,
  AfterViewInit,
} from "@angular/core";
import { Router } from "@angular/router";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort, Sort } from "@angular/material/sort";
import { tap } from "rxjs/operators";
import { wcmAnimations } from "bpw-common";
import { ProcessDefinitionsService } from "../../../../services/process-definitions.service";
@Component({
  selector: "camunda-dashboard-processes",
  templateUrl: "./processes.component.html",
  styleUrls: ["./processes.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ProcessesComponent implements OnInit, OnDestroy, AfterViewInit {
  itemCount: number;
  dataSource: MatTableDataSource<any>;
  displayedColumns = [
    "state",
    "incidents",
    "runnningInstances",
    "name",
    "tenantID",
  ];
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort: MatSort;
  private noData: any[] = [<any>{}];
  defaultSort: Sort = { active: "name", direction: "asc" };

  constructor(
    private _router: Router,
    private _processDefinitionsService: ProcessDefinitionsService
  ) {
    this.dataSource = new MatTableDataSource(this.noData);
  }

  ngOnInit(): void {
    this._loadPagedData();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  ngOnDestroy(): void {}
  itemSelected(i) {
    this._router.navigateByUrl(
      `/bpm/dashboard/processes/processDefinition?key=${this.dataSource.data[i].key}`
    );
  }

  private _loadPagedData() {
    this._processDefinitionsService
      .getStatisticsIncludeIncidents(true)
      .pipe(
        tap((processDefinitionStatistics) =>
          this._prepareTableDate(processDefinitionStatistics)
        )
      )
      .subscribe();
  }

  private _prepareTableDate(processDefinitionStatistics) {
    this.itemCount = processDefinitionStatistics.length;
    let items = [];
    for (let i = 0; i < processDefinitionStatistics.length; i++) {
      items.push({
        id: processDefinitionStatistics[i].id,
        key: processDefinitionStatistics[i].definition.key,
        suspended: processDefinitionStatistics[i].definition.suspended,
        incidents: processDefinitionStatistics[i].incidents.length,
        runnningInstances: processDefinitionStatistics[i].instances,
        name:
          processDefinitionStatistics[i].definition.name ||
          processDefinitionStatistics[i].definition.key,
        tenantID: "",
      });
    }
    this.dataSource.data = items.sort((a, b) => {
      var nameA = a.name.toUpperCase();
      var nameB = b.name.toUpperCase();
      if (nameA < nameB) {
        return -1;
      }
      if (nameB > nameA) {
        return 1;
      }
      return 0;
    });
  }
}
