import { Component, OnInit, Output, EventEmitter } from "@angular/core";

import { AclService } from "bpw-wcm-service";
import { GroupDataSource } from "./group.datasource";

@Component({
  selector: "group-selection",
  templateUrl: "./group-selection.component.html",
  styleUrls: ["./group-selection.component.scss"]
})
export class GroupSelectionComponent implements OnInit {
  selectedGroup: string;
  pageSize: number = 10;
  dataSource: GroupDataSource;
  @Output()
  groupSelected: EventEmitter<string> = new EventEmitter<string>();
  constructor(private aclService: AclService) {
    this.dataSource = new GroupDataSource(aclService, this.pageSize);
  }
  ngOnInit(): void {}

  addSelectedGroup() {
    this.groupSelected.emit(this.selectedGroup);
  }
}
