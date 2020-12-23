import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {
  RestClient,
  Body,
  Client,
  Post,
  Timeout,
  Path,
  Produces,
  Query,
  MediaType,
  ApiConfigService,
} from "bpw-rest-client";
@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "runtime-process-definition-service",
  baseUrl: "/camunda/api/runtime/process-definition",
  headers: {
    "content-type": "application/json",
  },
})
export class RuntimeProcessDefinitionService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("/{id}/called-process-definitions")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  calledProcessDefinitions(
    @Path("id") id: string,
    @Body query: any
  ): Observable<any> {
    return null;
  }
  // process-definition/invoice:1:a062dcc4-ac0c-11ea-b877-f2d5bf80b1aa/called-process-definitions
  // { "parentProcessDefinitionId": "invoice:2:a090a38d-ac0c-11ea-b877-f2d5bf80b1aa", "activityIdIn": [] }
}
