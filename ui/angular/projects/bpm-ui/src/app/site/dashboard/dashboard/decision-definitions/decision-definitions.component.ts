import { Component, OnInit } from "@angular/core";
import { tap } from "rxjs/operators";
import { DecisionDefinitionsService } from "../../../../services/decision-definitions.service";
@Component({
  // selector: "camunda-decision-definitions",
  templateUrl: "./decision-definitions.component.html",
  styleUrls: ["./decision-definitions.component.scss"],
})
export class DecisionDefinitionsComponent implements OnInit {
  count: number = 0;
  constructor(private decisionDefinitionsService: DecisionDefinitionsService) {}

  ngOnInit(): void {
    this.decisionDefinitionsService
      .getDecisionDefinitionsCount(true)
      .pipe(tap((result) => (this.count = result.count)))
      .subscribe();
  }
}
