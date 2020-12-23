import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable } from "rxjs";
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
  serviceId: "bpm-process-definition-service",
  baseUrl: "/camunda/api/engine/process-definition",
  headers: {
    "content-type": "application/json",
  },
})
export class ProcessDefinitionsService extends RestClient {
  onProcessDefIdChanged: BehaviorSubject<any>;
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
    this.onProcessDefIdChanged = new BehaviorSubject(null);
  }

  @Get("/")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getAllVersions(
    @Query("key") key: string,
    @Query("sortBy") sortBy: string,
    @Query("sortOrder") sortOrder: string,
    @Query("withoutTenantId") withoutTenantId: boolean
  ): Observable<any> {
    return null;
  }

  @Get("/statistics")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getStatisticsIncludeRootIncidents(
    @Query("rootIncidents") includeRootIncidents: boolean
    // @Query("failedJobs") includeFailedJobs?: boolean,
    // @Query("incidents") includeIncidents?: boolean,
    // @Query("incidentsForType") includeIncidentsForType?: boolean
  ): Observable<any> {
    return null;
  }

  @Get("/statistics")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getStatisticsIncludeIncidents(
    @Query("incidents") includeIncidents: boolean
  ): Observable<any> {
    return null;
  }

  @Get("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getProcessDefinitionsCount(
    @Query("latestVersion") latestVersion: boolean
  ): Observable<any> {
    return null;
  }
}
