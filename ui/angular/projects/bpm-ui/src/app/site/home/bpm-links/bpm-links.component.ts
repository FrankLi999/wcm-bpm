import { Component, OnInit, Input } from "@angular/core";
import { BpmContentService } from "../../../services/bpm-content.service";
import { BpmLinks } from "../../../model/bpm-links.model";
@Component({
  // selector: "bpm-links",
  templateUrl: "./bpm-links.component.html",
  styleUrls: ["./bpm-links.component.scss"],
})
export class BpmLinksComponent implements OnInit {
  constructor(private _bpmContentService: BpmContentService) {}
  @Input() wcmpath = "";
  bpmLinks: BpmLinks;
  ngOnInit(): void {
    this._bpmContentService
      .getBpmLinks(this.wcmpath)
      .subscribe((links) => (this.bpmLinks = links));
  }
}
