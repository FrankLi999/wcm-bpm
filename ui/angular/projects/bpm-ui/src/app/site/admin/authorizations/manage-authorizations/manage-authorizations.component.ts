import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { tap } from "rxjs/operators";
import { SidebarService } from "bpw-common";
import { BpmContentService } from "../../../../services/bpm-content.service";
import { Resource } from "../../../../model/Resource";
import { AuthorizationService } from "../../../../services/authorization.service";

@Component({
  //selector: "manage-authorizations",
  templateUrl: "./manage-authorizations.component.html",
  styleUrls: ["./manage-authorizations.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class ManageAuthorizationsComponent implements OnInit {
  resources: Resource[];
  // selectedResourceIndex: number = 13;
  constructor(
    private authorizationService: AuthorizationService,
    private contentService: BpmContentService,
    private sidebarService: SidebarService
  ) {}

  ngOnInit(): void {
    this.contentService
      .getResources()
      .pipe(
        tap((resources) => {
          this.resources = resources;
          this.resources.forEach((resources) => {
            resources.title =
              resources.title ||
              resources.resource.split("_").slice(1).join(" ");
          });
          this.authorizationService.onResourceChanged.next(this.resources[0]);
        })
      )
      .subscribe();
  }

  // ngAfterViewInit() {
  //   const content = document.getElementById("test-render-widget");
  //   let test =
  //     '<render-widget component="UserListComponent" data=\'{"data":"test"}\'></render-widget>';
  //   console.log(test);
  //   content.innerHTML = test;
  // }

  toggleSidebar(name): void {
    this.sidebarService.getSidebar(name).toggleOpen();
  }

  resourceChanged(selected) {
    this.authorizationService.onResourceChanged.next(this.resources[selected]);
  }
}
