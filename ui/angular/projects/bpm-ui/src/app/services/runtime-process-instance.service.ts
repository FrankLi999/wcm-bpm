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
  serviceId: "runtime-process-instance-service",
  baseUrl: "/camunda/api/runtime/process-instance",
  headers: {
    "content-type": "application/json",
  },
})
export class RuntimeProcessInstanceService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  getProcessInstances(
    @Query("firstResult") firstResult: number,
    @Query("maxResults") maxResults: number,
    @Query("sortBy") sortBy: string,
    @Query("sortOrder") sortOrder: string,
    @Body processInstanceQuery: any
  ): Observable<any> {
    return null;
  }
  //http://localhost:28080/camunda/api/runtime/process-instance/?firstResult=0&maxResults=50&sortBy=startTime&sortOrder=desc

  // 	{
  //   "firstResult": 0,
  //     "maxResults": 50,
  //       "sortBy": "startTime",
  //         "sortOrder": "desc",
  //           "processDefinitionId": "invoice:1:a062dcc4-ac0c-11ea-b877-f2d5bf80b1aa"
  // }
}
