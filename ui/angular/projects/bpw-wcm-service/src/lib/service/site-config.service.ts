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
import { SiteConfig } from "../model/SiteConfig";
import { PageConfig } from "../model/PageConfig";
@Injectable({
  providedIn: "root"
})
@Client({
  serviceId: "modeshape-service",
  baseUrl: "/wcm/api/siteConfig",
  headers: {
    "content-type": "application/json"
  }
})
export class SiteConfigService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createSiteConfig(@Body siteConfig: SiteConfig): Observable<any> {
    return null;
  }

  @Put("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public saveSiteConfig(@Body siteConfig: SiteConfig): Observable<any> {
    return null;
  }

  @Get("/get/{repository}/{workspace}/{library}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getSiteConfigs(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Path("library") library: string
  ): Observable<SiteConfig[]> {
    return null;
  }

  @Put("/lock/{repository}/{workspace}/{library}/{siteConfig}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public lockSiteConfig(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Path("library") library: string,
    @Path("siteConfig") siteConfig: string
  ): Observable<SiteConfig> {
    return null;
  }

  @Get("/pageConfig/{repository}/{workspace}/{library}/{siteConfig}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getPageConfig(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Path("library") library: string,
    @Path("siteConfig") siteConfig: string
  ): Observable<PageConfig> {
    return null;
  }

  @Delete("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public purgeSiteConfig(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }
}
