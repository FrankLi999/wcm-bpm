import {
  Component,
  Input,
  OnInit,
  ViewChild,
  OnDestroy,
  AfterViewInit,
} from "@angular/core";
import { Router } from "@angular/router";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort, Sort } from "@angular/material/sort";
import { filter, takeUntil, tap } from "rxjs/operators";
import { merge, Subject, Subscription } from "rxjs";
import { RuntimeProcessInstanceService } from "../../../../../services/runtime-process-instance.service";
import { ProcessDefinitionsService } from "../../../../../services/process-definitions.service";
@Component({
  selector: "process-instance-table",
  templateUrl: "./process-instance-table.component.html",
  styleUrls: ["./process-instance-table.component.scss"],
})
export class ProcessInstanceTableComponent
  implements OnInit, OnDestroy, AfterViewInit {
  @Input() processDefinitionId: string;
  itemCount: number;
  dataSource: MatTableDataSource<any>;
  displayedColumns = ["state", "id", "startTime", "businessKey"];
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort: MatSort;
  defaultSort: Sort = { active: "startTime", direction: "desc" };
  private noData: any[] = [<any>{}];
  private _unsubscribeAll: Subject<any>;
  private subscription: Subscription = new Subscription();
  private _pageSize: number = 50;
  constructor(
    private _router: Router,
    private _processInstanceService: RuntimeProcessInstanceService,
    private _processDefinitionsService: ProcessDefinitionsService
  ) {
    this.dataSource = new MatTableDataSource(this.noData);
    this._unsubscribeAll = new Subject();
  }

  ngOnInit(): void {
    //
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this._unsubscribeAll.next();
    this._unsubscribeAll.complete();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this._loadPagedData();
    this._processDefinitionsService.onProcessDefIdChanged
      .pipe(
        takeUntil(this._unsubscribeAll),
        filter((data) => !!data && data.currentTab === "processInstance"),
        tap((data) => {
          this.processDefinitionId = data.processDefinitionId;
          this._loadPagedData();
        })
      )
      .subscribe();

    let sort$ = this.sort.sortChange.pipe(
      tap(() => (this.paginator.pageIndex = 0))
    );

    this.subscription.add(
      merge(sort$, this.paginator.page)
        .pipe(tap(() => this._loadPagedData()))
        .subscribe()
    );
  }
  processInstanceSelected(id: string) {
    this._router.navigateByUrl(
      `/bpm/dashboard/processes/processInstance?id=${id}`
    );
  }
  private _loadPagedData() {
    this._processInstanceService
      .getProcessInstances(0, 50, "startTime", "desc", {
        firstResult: this._pageSize * this.paginator.pageIndex,
        maxResults: this._pageSize,
        sortBy: "startTime",
        sortOrder: "desc",
        processDefinitionId: this.processDefinitionId,
      })
      .pipe(tap((processInstances) => this._prepareTableDate(processInstances)))
      .subscribe();
  }

  private _prepareTableDate(processInstances) {
    this.itemCount = processInstances.length;
    this.dataSource.data = processInstances;
  }
}
