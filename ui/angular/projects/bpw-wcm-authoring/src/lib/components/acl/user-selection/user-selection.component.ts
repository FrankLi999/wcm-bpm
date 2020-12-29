import { Component, OnInit, Output, EventEmitter } from "@angular/core";

import { AclService } from "bpw-wcm-service";
import { UserDataSource } from "./user.datasource";

@Component({
  selector: "user-selection",
  templateUrl: "./user-selection.component.html",
  styleUrls: ["./user-selection.component.scss"]
})
export class UserSelectionComponent implements OnInit {
  selectedUser: string;
  pageSize: number = 10;
  dataSource: UserDataSource;
  @Output()
  userSelected: EventEmitter<string> = new EventEmitter<string>();

  constructor(private aclService: AclService) {
    this.dataSource = new UserDataSource(aclService, this.pageSize);
  }
  ngOnInit(): void {}

  addSelectedUser() {
    this.userSelected.emit(this.selectedUser);
  }
}
