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
import { RenderTemplate } from "../model/RenderTemplate";
@Injectable({
  providedIn: "root"
})
@Client({
  serviceId: "modeshape-service",
  baseUrl: "/wcm/api/rt",
  headers: {
    "content-type": "application/json"
  }
})
export class RtService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }
  @Get("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getRenderTemplates(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string
  ): Observable<{ [key: string]: RenderTemplate }> {
    return null;
  }

  @Get("/get/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getRenderTemplate(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") renderTemplatePath: string
  ): Observable<RenderTemplate> {
    return null;
  }

  @Put("/lock/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public lockRenderTemplate(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") renderTemplatePath: string
  ): Observable<RenderTemplate> {
    return null;
  }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createRenderTemplate(@Body rt: RenderTemplate): Observable<any> {
    return null;
  }

  @Put("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public saveRenderTemplate(@Body rt: RenderTemplate): Observable<any> {
    return null;
  }

  @Delete("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public purgeRenderTemplate(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }
}
