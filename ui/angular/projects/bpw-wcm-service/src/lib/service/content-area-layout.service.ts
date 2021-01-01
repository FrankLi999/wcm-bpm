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
  ApiConfigService
} from "bpw-rest-client";
import { ContentAreaLayout } from "../model/ContentAreaLayout";
@Injectable({
  providedIn: "root"
})
@Client({
  serviceId: "wcm",
  baseUrl: "/wcm/api/contentAreaLayout",
  headers: {
    "content-type": "application/json"
  }
})
export class ContentAreaLayoutService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createContentAreaLayout(
    @Body contentAreaLayout: ContentAreaLayout
  ): Observable<any> {
    return null;
  }

  @Put("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public saveContentAreaLayout(
    @Body contentAreaLayout: ContentAreaLayout
  ): Observable<any> {
    return null;
  }

  @Get("/list/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getContentAreaLayouts(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string
  ): Observable<{ [key: string]: ContentAreaLayout }> {
    return null;
  }

  @Get("/get/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getContentAreaLayout(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") renderTemplatePath: string
  ): Observable<ContentAreaLayout> {
    return null;
  }

  @Put("/lock/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public lockContentAreaLayout(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") renderTemplatePath: string
  ): Observable<{ [key: string]: ContentAreaLayout }> {
    return null;
  }

  @Delete("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public purgeContentAreaLayout(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }
}
