import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import {
  RestClient,
  Client,
  Delete,
  Get,
  Post,
  Put,
  Body,
  Path,
  Timeout,
  Produces,
  Query,
  MediaType,
  ApiConfigService
} from "bpw-rest-client";
import { BpmnWorkflow } from "../model/BpmnWorkflow";

@Injectable({
  providedIn: "root"
})
@Client({
  serviceId: "modeshape-service",
  baseUrl: "/wcm/api/bpmnWorkflow",
  headers: {
    "content-type": "application/json"
  }
})
export class WorkflowService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public loadBpmnWorkflows(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("filter") filter: string = "",
    @Query("sort") sortDirection: string = "asc",
    @Query("pageIndex") pageIndex: number = 0,
    @Query("pageSize") pageSize: number = 3
  ): Observable<BpmnWorkflow[]> {
    return null;
  }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createBpmnWorkflow(@Body bpmnWorkflow: BpmnWorkflow): Observable<any> {
    return null;
  }

  @Put("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public saveBpmnWorkflow(@Body bpmnWorkflow: BpmnWorkflow): Observable<any> {
    return null;
  }

  @Delete("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public purgeWorkflow(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }
}
