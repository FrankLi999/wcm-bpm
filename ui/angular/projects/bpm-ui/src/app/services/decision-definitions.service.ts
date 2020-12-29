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
  serviceId: "bpm-decision-service",
  baseUrl: "/camunda/api/engine/decision-definition",
  headers: {
    "content-type": "application/json",
  },
})
export class DecisionDefinitionsService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getDecisionDefinitionsCount(
    @Query("latestVersion") latestVersion: boolean
  ): Observable<any> {
    return null;
  }
}
