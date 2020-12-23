import { Component, OnInit } from "@angular/core";
import { tap } from "rxjs/operators";
import { CaseDefinitionsService } from "../../../../services/case-definitions.service";
@Component({
  // selector: "camunda-case-definitions",
  templateUrl: "./case-definitions.component.html",
  styleUrls: ["./case-definitions.component.scss"],
})
export class CaseDefinitionsComponent implements OnInit {
  count: number = 0;
  constructor(private caseDefinitionsService: CaseDefinitionsService) {}

  ngOnInit(): void {
    this.caseDefinitionsService
      .getCaseDefinitionsCount(true)
      .pipe(tap((result) => (this.count = result.count)))
      .subscribe();
  }
}
