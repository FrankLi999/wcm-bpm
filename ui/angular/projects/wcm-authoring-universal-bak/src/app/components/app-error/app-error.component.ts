import { Component, OnInit, Input } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { appConfig } from "bpw-common";
// declare var appConfig: any;
@Component({
  selector: "app-app-error",
  templateUrl: "./app-error.component.html",
  styleUrls: ["./app-error.component.scss"],
})
export class AppErrorComponent implements OnInit {
  errorMessage$: Observable<string>;
  siteUrl: string;
  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.siteUrl = appConfig.defaultUrl;
    this.errorMessage$ = this.route.queryParamMap.pipe(
      map((params) => params.get("errorMessage"))
    );
  }
}
