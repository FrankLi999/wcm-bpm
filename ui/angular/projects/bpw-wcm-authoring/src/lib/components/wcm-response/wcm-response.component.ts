import { Component, OnInit, Input } from "@angular/core";
import { Observable } from "rxjs";
import { UiService } from "bpw-common";
import { WCM_ACTION_SUCCESSFUL } from "bpw-wcm-service";

@Component({
  selector: "wcm-response",
  templateUrl: "./wcm-response.component.html",
  styleUrls: ["./wcm-response.component.css"],
})
export class WcmResponseComponent implements OnInit {
  @Input() status$: Observable<any>;
  @Input() successMessage: string;
  constructor(private uiService: UiService) {}

  ngOnInit(): void {}

  isSucessfule(status: any): boolean {
    return status === WCM_ACTION_SUCCESSFUL;
  }

  errorMessage(status: any) {
    return this.uiService.getErrorMessage(status);
  }
}
