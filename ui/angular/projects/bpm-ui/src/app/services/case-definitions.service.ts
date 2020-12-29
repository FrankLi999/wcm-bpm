import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {
  RestClient,
  Client,
  Get,
  Timeout,
  Produces,
  Query,
  MediaType,
  ApiConfigService,
} from "bpw-rest-client";
@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "bpm-case-service",
  baseUrl: "/camunda/api/engine/case-definition",
  headers: {
    "content-type": "application/json",
  },
})
export class CaseDefinitionsService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getCaseDefinitionsCount(
    @Query("latestVersion") latestVersion: boolean
  ): Observable<any> {
    return null;
  }
}
