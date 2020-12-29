import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewChild,
  Input,
} from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { Store } from "@ngrx/store";
import { BehaviorSubject, Subscription } from "rxjs";
import * as fromStore from "bpw-wcm-service";
import {
  HistoryService,
  WcmVersion,
  WcmHistory,
  WcmConstants,
} from "bpw-wcm-service";

@Component({
  selector: "wcm-history",
  templateUrl: "./history.component.html",
  styleUrls: ["./history.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class HistoryComponent implements OnInit, OnDestroy {
  @Input() repository: string = WcmConstants.REPO_BPWIZARD;
  @Input() workspace: string = WcmConstants.WS_DEFAULT;
  @Input() wcmPath: string;
  @Input() wcmVersions: WcmVersion[] = [];
  @Input() historySubject: BehaviorSubject<WcmVersion[]>;
  updateStatus: string;
  sub: Subscription;
  dataSource: MatTableDataSource<WcmVersion>;
  @ViewChild("versionTable", { static: true }) versionTable: MatTable<
    WcmVersion
  >;
  displayedColumns = ["versionName", "created", "actions"];

  constructor(
    private historyService: HistoryService,
    private store: Store<fromStore.WcmAppState>,
    private route: ActivatedRoute,
    protected router: Router
  ) {}

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource(
      this.wcmVersions.length ? this.wcmVersions : [<WcmVersion>{}]
    );
    this.sub = this.historySubject.subscribe((versions) => {
      if (versions) {
        this.wcmVersions = versions;
        this.dataSource.data = this.wcmVersions;
      }
    });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  restore(index: number) {
    this.historyService
      .restore(
        this.repository,
        this.workspace,
        this.wcmPath,
        this.wcmVersions[index].name
      )
      .subscribe(
        (resp: any) => {
          this.updateStatus = "Rstore successfully";
          this.reload();
        },
        (response) => {
          this.updateStatus = response.error;
        },
        () => {
          // console.log("SaveGrants is now completed.");
        }
      );
  }

  private reload() {
    this.historyService
      .getHistory(this.repository, this.workspace, this.wcmPath)
      .subscribe(
        (history: WcmHistory) => {
          this.updateStatus = "Reload history successfully";
          this.wcmVersions = history.versions;
          this.dataSource.data = this.wcmVersions;
        },
        (response) => {
          this.updateStatus = response.error;
        },
        () => {
          // console.log("SaveGrants is now completed.");
        }
      );
  }
}
