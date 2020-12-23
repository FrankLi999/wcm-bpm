import { Component, OnInit, Input } from "@angular/core";

@Component({
  // selector: 'link-element',
  templateUrl: "./link-element.component.html",
  styleUrls: ["./link-element.component.scss"],
})
export class LinkElementComponent implements OnInit {
  @Input() link: string;
  @Input() contentpath: string;
  @Input() title: string;
  @Input() hint: string;
  @Input() contentparameter: string;
  constructor() {}

  ngOnInit(): void {
    this.contentpath = this.contentpath || "/mysite/rootSiteArea/news/news1";
    this.contentparameter = this.contentparameter || "newsItem";
  }

  get state() {
    let state = {};
    state[this.contentparameter] = this.contentpath;
    return state;
  }
}
