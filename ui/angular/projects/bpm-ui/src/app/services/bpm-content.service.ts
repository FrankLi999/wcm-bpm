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

import { BpmApplications } from "../model/bpm-applications.model";
import { BpmLinks } from "../model/bpm-links.model";
import { Resource } from "../model/Resource";

@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "bpm-content-service",
  baseUrl: "/bpm/content",
  headers: {
    "content-type": "application/json",
  },
})
export class BpmContentService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("/application")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getBpmApplications(
    @Query("wcmPath") wcmPath: string
  ): Observable<BpmApplications> {
    return null;
  }

  @Get("/link")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getBpmLinks(@Query("wcmPath") wcmPath: string): Observable<BpmLinks> {
    return null;
  }

  @Get("/resources")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getResources(): Observable<Resource[]> {
    return null;
  }
}
