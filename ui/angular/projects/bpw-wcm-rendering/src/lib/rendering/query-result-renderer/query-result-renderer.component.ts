import { Component, OnInit, Input } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
import { BehaviorSubject } from "rxjs";
import { filter } from "rxjs/operators";
import {
  RenderTemplate,
  QueryStatementService,
  RendererService,
  QueryStatement,
  QueryResult,
  WcmUtils,
} from "bpw-wcm-service";

@Component({
  selector: "query-result-renderer",
  templateUrl: "./query-result-renderer.component.html",
  styleUrls: ["./query-result-renderer.component.scss"],
})
export class QueryResultRendererComponent implements OnInit {
  @Input() renderer;
  @Input() rendererTemplate: RenderTemplate;
  @Input() repository: string;
  @Input() workspace: string;
  @Input() siteAreaKey: string;
  queryResult: QueryResult;
  queryResultChange = new BehaviorSubject<QueryResult>(null);
  constructor(
    private queryStatementService: QueryStatementService,
    private sanitizer: DomSanitizer,
    private rendererService: RendererService
  ) {}

  ngOnInit(): void {
    this.sanitizer.bypassSecurityTrustHtml(this.rendererTemplate.code);
    let query: QueryStatement = {
      repository: this.repository,
      workspace: this.workspace,
      library: WcmUtils.library(this.rendererTemplate.resourceName),
      name: WcmUtils.itemName(this.rendererTemplate.resourceName),
    };
    this.queryStatementService
      .executeQueryStatement(query)
      .pipe(filter((queryResult) => !!queryResult))
      .subscribe((queryResult) => {
        this.queryResult = queryResult;
        this.rendererService.addQueryResult(
          this.queryResultId(),
          this.queryResult
        );
        this.queryResultChange.next(this.queryResult);
      });
  }
  ngOnDestroy() {
    this.queryResultChange.unsubscribe();
  }
  queryResultId(): string {
    return `${this.renderer}_${this.siteAreaKey}_${this.renderer}`;
    // _${this.contentPathIndex}`;
  }
}
