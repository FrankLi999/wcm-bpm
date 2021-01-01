import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import {
  RestClient,
  Client,
  Get,
  Post,
  Put,
  Delete,
  Body,
  Path,
  Timeout,
  Produces,
  Query,
  MediaType,
  ApiConfigService,
} from "bpw-rest-client";

import { JsonForm } from "../model/JsonForm";
import { Theme } from "../model/Theme";
import { ControlField } from "../model/ControlField";
import { WcmSystem } from "../model/WcmSystem";

import { WcmNode } from "../model/WcmNode";
import { WcmItemFilter } from "../model/WcmItemFilter";

@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "wcm",
  baseUrl: "/wcm/api",
  headers: {
    "content-type": "application/json",
  },
})
export class WcmService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("/theme/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getTheme(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string
  ): Observable<Theme[]> {
    return null;
  }

  @Get("/control/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getControlField(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string
  ): Observable<ControlField[]> {
    return null;
  }

  @Get("/wcmSystem/{repository}/{workspace}/{library}/{siteConfig}")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public getWcmSystemForAuthoring(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Path("library") library: string,
    @Path("siteConfig") siteConfig: string
  ): Observable<WcmSystem> {
    return null;
  }

  @Get(
    "/wcmSystem/{repository}/{workspace}/{library}/{siteConfig}?authoring=false"
  )
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public getWcmSystem(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Path("library") library: string,
    @Path("siteConfig") siteConfig: string
  ): Observable<WcmSystem> {
    return null;
  }

  @Get("/jsonform/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getJsonForms(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string
  ): Observable<{ [key: string]: JsonForm[] }> {
    return null;
  }

  @Put("/wcmItem/unlock/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public unlock(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<null> {
    return null;
  }

  @Put("/wcmItem/restore/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public restore(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string,
    @Query("version") version: string
  ): Observable<null> {
    return null;
  }

  // @Delete("/wcmItem/purge/{repository}/{workspace}")
  // @Timeout(2000) //In milliseconds
  // @Produces(MediaType.JSON)
  // public purgeWcmItem(
  //   @Path("repository") repository: string,
  //   @Path("workspace") workspace: string,
  //   @Query("path") absPath: string
  // ): Observable<any> {
  //   return null;
  // }

  @Post("/wcmNodes/{repository}/{workspace}")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  getWcmNodes(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Body filter: WcmItemFilter
  ): Observable<WcmNode[]> {
    return null;
  }
}
