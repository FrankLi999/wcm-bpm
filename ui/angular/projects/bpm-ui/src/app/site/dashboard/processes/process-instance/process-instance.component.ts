import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
@Component({
  selector: "process-instance",
  templateUrl: "./process-instance.component.html",
  styleUrls: ["./process-instance.component.scss"],
})
export class ProcessInstanceComponent implements OnInit {
  processInstanceId: string;
  constructor(private _route: ActivatedRoute) {}

  ngOnInit(): void {
    this.processInstanceId = this._route.snapshot.queryParams["id"];
  }
}
