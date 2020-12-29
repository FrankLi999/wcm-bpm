import { Component, OnInit, Input } from "@angular/core";
import { WcmConfigService } from "bpw-wcm-service";

@Component({
  selector: "query-editor",
  templateUrl: "./query-editor.component.html",
  styleUrls: ["./query-editor.component.scss"],
})
export class QueryEditorComponent implements OnInit {
  @Input() queryName: string;
  constructor(protected wcmConfigService: WcmConfigService) {}

  ngOnInit() {}
}
