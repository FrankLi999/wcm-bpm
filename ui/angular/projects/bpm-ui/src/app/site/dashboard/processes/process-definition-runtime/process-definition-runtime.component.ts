import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { SidebarService, wcmAnimations } from "bpw-common";
import { forkJoin } from "rxjs";
import { tap } from "rxjs/operators";
import { PerfectScrollbarConfigInterface } from "ngx-perfect-scrollbar";
import { ProcessDefinitionsService } from "../../../../services/process-definitions.service";
import { ProcessInstanceService } from "../../../../services/process-instance.service";
@Component({
  selector: "process-definition-runtime",
  templateUrl: "./process-definition-runtime.component.html",
  styleUrls: ["./process-definition-runtime.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ProcessDefinitionRuntimeComponent implements OnInit {
  pdfVersions: any[];
  versionNumbers: string[] = [];
  currentVersion: any;
  totalInstances: number;
  currentInstances: number;
  selectedTab: number = 0;
  key: string;
  tabs: string[] = [
    "processInstance",
    "incidents",
    "calledProcessDefinitions",
    "businessKet",
  ];
  public config: PerfectScrollbarConfigInterface = {};
  constructor(
    private _sidebarService: SidebarService,
    private _route: ActivatedRoute,
    private _processDefinitionsService: ProcessDefinitionsService,
    private _processInstanceService: ProcessInstanceService
  ) {}

  ngOnInit(): void {
    this.key = this._route.snapshot.queryParams["key"];
    this._processDefinitionsService
      .getAllVersions(this.key, "version", "desc", true)
      .pipe(
        tap((versions) => {
          this.pdfVersions = versions;
          this.currentVersion = versions[0];
          for (let i = 0; i < this.pdfVersions.length; i++) {
            this.versionNumbers.push(this.pdfVersions[i].version);
          }
          this._getProcessInstanceCounts();
        })
      )
      .subscribe();
  }
  selectedIndexChange(index) {
    this.selectedTab = index;
    this._processDefinitionsService.onProcessDefIdChanged.next({
      processDefinitionId: this.currentVersion.id,
      currentTab: this.tabs[this.selectedTab],
    });
  }

  toggleSidebar(name): void {
    this._sidebarService.getSidebar(name).toggleOpen();
  }

  switchToVersion(v) {
    for (let i = 0; i < this.pdfVersions.length; i++) {
      if (v === this.pdfVersions[i].version) {
        this.currentVersion = this.pdfVersions[i];
        break;
      }
    }
    this._processDefinitionsService.onProcessDefIdChanged.next({
      processDefinitionId: this.currentVersion.id,
      currentTab: this.tabs[this.selectedTab],
    });
    this._getProcessInstanceCounts();
  }

  private _getProcessInstanceCounts() {
    forkJoin({
      totalInstances: this._processInstanceService.getProcessInstanceCount({
        processDefinitionKey: this.currentVersion.key,
        processDefinitionWithoutTenantId: true,
      }),
      currentInstances: this._processInstanceService.getProcessInstanceCount({
        processDefinitionId: this.currentVersion.id,
      }),
    })
      .pipe(
        tap((data) => {
          this.totalInstances = data.totalInstances.count;
          this.currentInstances = data.currentInstances.count;
        })
      )
      .subscribe();
  }
}
