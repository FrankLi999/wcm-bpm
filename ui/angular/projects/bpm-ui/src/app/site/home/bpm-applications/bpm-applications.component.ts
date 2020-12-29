import { Component, OnInit, Input } from "@angular/core";
import { BpmContentService } from "../../../services/bpm-content.service";
import { BpmApplications } from "../../../model/bpm-applications.model";
@Component({
  // selector: "bpm-applications",
  templateUrl: "./bpm-applications.component.html",
  styleUrls: ["./bpm-applications.component.scss"],
})
export class BpmApplicationsComponent implements OnInit {
  constructor(private _bpmContentService: BpmContentService) {}
  @Input() wcmpath: string;
  bpmApplications: BpmApplications;
  ngOnInit(): void {
    this._bpmContentService
      .getBpmApplications(this.wcmpath)
      .subscribe((apps) => (this.bpmApplications = apps));
  }
}
