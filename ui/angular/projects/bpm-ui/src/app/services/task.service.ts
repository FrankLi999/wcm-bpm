import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {
  RestClient,
  Client,
  Get,
  Query,
  Timeout,
  Produces,
  MediaType,
  ApiConfigService,
} from "bpw-rest-client";

@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "bpm-task-service",
  baseUrl: "/camunda/api/engine/task",
  headers: {
    "content-type": "application/json",
  },
})
@Injectable({
  providedIn: "root",
})
export class TaskService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getTasksCount(): Observable<any> {
    return null;
  }

  @Get("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getAssigned(
    @Query("unfinished") unfinished: boolean,
    @Query("assigned") assigned: boolean
  ): Observable<any> {
    return null;
  }

  @Get("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getUnassignedWithCandidateGroup(
    @Query("unfinished") unfinished: boolean,
    @Query("unassigned") unassigned: boolean,
    @Query("withCandidateGroups") withCandidateGroups: boolean
  ): Observable<any> {
    return null;
  }

  @Get("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getUnassignedWithoutCandidateGroup(
    @Query("unfinished") unfinished: boolean,
    @Query("unassigned") unassigned: boolean,
    @Query("withoutCandidateGroups") withoutCandidateGroups: boolean
  ): Observable<any> {
    return null;
  }
}
