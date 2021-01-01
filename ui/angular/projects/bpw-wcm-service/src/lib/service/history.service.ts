import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import {
  RestClient,
  Client,
  Get,
  Put,
  Path,
  Timeout,
  Produces,
  Query,
  MediaType,
  ApiConfigService
} from "bpw-rest-client";

import { WcmHistory } from "../model/WcmHistory";
@Injectable({
  providedIn: "root"
})
@Client({
  serviceId: "wcm",
  baseUrl: "/wcm/api/history",
  headers: {
    "content-type": "application/json"
  }
})
export class HistoryService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }
  @Get("/library/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getLibraryHistory(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") wcmPath: string = ""
  ): Observable<WcmHistory> {
    return null;
  }

  @Get("/item/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getHistory(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") wcmPath: string = ""
  ): Observable<WcmHistory> {
    return null;
  }

  @Put("/item/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  public restore(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") wcmPath: string = "",
    @Query("version") versionName: string = ""
  ): Observable<any> {
    return null;
  }
}
