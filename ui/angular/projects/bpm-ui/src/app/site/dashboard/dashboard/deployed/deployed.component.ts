import { Component, OnInit } from "@angular/core";
import { tap } from "rxjs/operators";
import { DeploymentService } from "../../../../services/deployment.service";
@Component({
  // selector: "camunda-deployed",
  templateUrl: "./deployed.component.html",
  styleUrls: ["./deployed.component.scss"],
})
export class DeployedComponent implements OnInit {
  count: number = 0;
  constructor(private deploymentService: DeploymentService) {}

  ngOnInit(): void {
    this.deploymentService
      .getDeploymentsCount()
      .pipe(tap((result) => (this.count = result.count)))
      .subscribe();
  }
}
