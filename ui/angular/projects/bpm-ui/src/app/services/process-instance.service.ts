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
  serviceId: "bpm-process-instance-service",
  baseUrl: "/camunda/api/engine/process-instance",
  headers: {
    "content-type": "application/json",
  },
})
export class ProcessInstanceService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getProcessInstanceCount(@Body query: any): Observable<any> {
    return null;
  }

  getProcessInstances() {}
}
