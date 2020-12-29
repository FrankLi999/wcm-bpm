import { Component, OnInit } from "@angular/core";
import { tap } from "rxjs/operators";
import { ProcessDefinitionsService } from "../../../../services/process-definitions.service";
@Component({
  // selector: "camunda-process-definitions",
  templateUrl: "./process-definitions.component.html",
  styleUrls: ["./process-definitions.component.scss"],
})
export class ProcessDefinitionsComponent implements OnInit {
  count: number = 0;
  constructor(private processDefinitionsService: ProcessDefinitionsService) {}

  ngOnInit(): void {
    this.processDefinitionsService
      .getProcessDefinitionsCount(true)
      .pipe(tap((result) => (this.count = result.count)))
      .subscribe();
  }
}
