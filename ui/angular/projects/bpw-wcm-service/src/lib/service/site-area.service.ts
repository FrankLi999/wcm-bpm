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
import { SiteArea } from "../model/SiteArea";
@Injectable({
  providedIn: "root"
})
@Client({
  serviceId: "wcm",
  baseUrl: "/wcm/api/sitearea",
  headers: {
    "content-type": "application/json"
  }
})
export class SiteAreaService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createSiteArea(@Body sa: SiteArea): Observable<any> {
    return null;
  }

  @Put("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public saveSiteArea(@Body sa: SiteArea): Observable<any> {
    return null;
  }

  @Get("/get/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getSiteArea(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") contentItemPath: string
  ): Observable<SiteArea> {
    return null;
  }

  @Put("/lock/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public lockSiteArea(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") contentItemPath: string
  ): Observable<SiteArea> {
    return null;
  }

  @Delete("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public purgeSiteArea(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }
}
