import { Component, OnInit, Input } from "@angular/core";
import { WcmWidget } from "bpw-wcm-service";
@Component({
  selector: "app-user-list",
  templateUrl: "./user-list.component.html",
  styleUrls: ["./user-list.component.scss"],
})
export class UserListComponent implements OnInit, WcmWidget {
  @Input() data: any;
  constructor() {}

  ngOnInit(): void {
    console.log(
      ">>>>>UserListComponentt>>>>>>> 1,",
      this.data,
      typeof this.data
    );
    console.log(
      ">>>>>UserListComponentt>>>>>>> 2,",
      JSON.parse('{"data": "xxx"}')
    );
    console.log(">>>>>UserListComponentt>>>>>>>3,", JSON.parse(this.data));
  }
}
