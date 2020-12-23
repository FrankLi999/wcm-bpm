import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {
  RestClient,
  Body,
  Client,
  Post,
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
  serviceId: "runtime-incident-service",
  baseUrl: "/camunda/api/runtime/incident",
  headers: {
    "content-type": "application/json",
  },
})
export class RuntimeIncidentService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  getIncidents(
    @Query("firstResult") firstResult: number,
    @Query("maxResults") maxResults: number,
    @Body incidentQuery: any
  ): Observable<any> {
    return null;
  }

  @Post("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  getIncidentCount(@Body incidentQuery: any): Observable<any> {
    return null;
  }

  // http://localhost:8080/camunda/api/cockpit/plugin/base/default/incident/?firstResult=0&maxResults=50

  //   { "processDefinitionIdIn": ["invoice:1:a062dcc4-ac0c-11ea-b877-f2d5bf80b1aa"], "activityIdIn": [] }
}
