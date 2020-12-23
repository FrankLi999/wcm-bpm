import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {
  RestClient,
  Body,
  Client,
  Get,
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
  serviceId: "job-definition-service",
  baseUrl: "/camunda/api/runtime/job-definition",
  headers: {
    "content-type": "application/json",
  },
})
export class JobDefinitionService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  getJobDefinitionCount(
    @Query("processDefinitionId") processDefinitionId: string
  ): Observable<any> {
    return null;
  }

  @Post("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  jobDefinitionCount(@Body query: any): Observable<any> {
    return null;
  }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  jobDefinitions(@Body query: any): Observable<any> {
    return null;
  }

  @Get("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  getJobDefinitions(
    @Query("firstResult") firstResult: string,
    @Query("maxResults") maxResults: string,
    @Query("processDefinitionId") processDefinitionId: string
  ): Observable<any> {
    return null;
  }
}
