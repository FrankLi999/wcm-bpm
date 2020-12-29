import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {
  RestClient,
  Client,
  Get,
  Timeout,
  Produces,
  MediaType,
  ApiConfigService,
} from "bpw-rest-client";
@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "bpm-engine-service",
  baseUrl: "/camunda/api/engine/engine",
  headers: {
    "content-type": "application/json",
  },
})
export class EngineService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getProcessEngineNames(): Observable<any> {
    return null;
  }
}
