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
  serviceId: "bpm-deploy-service",
  baseUrl: "/camunda/api/engine/deployment",
  headers: {
    "content-type": "application/json",
  },
})
export class DeploymentService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getDeploymentsCount(): Observable<any> {
    return null;
  }
}
