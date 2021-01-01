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
import { AuthoringTemplate } from "../model/AuthoringTemplate";
@Injectable({
  providedIn: "root"
})
@Client({
  serviceId: "wcm",
  baseUrl: "/wcm/api/at",
  headers: {
    "content-type": "application/json"
  }
})
export class AtService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public createAuthoringTemplate(@Body at: AuthoringTemplate): Observable<any> {
    return null;
  }

  @Put("")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public saveAuthoringTemplate(@Body at: AuthoringTemplate): Observable<any> {
    return null;
  }

  @Get("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getAuthoringTemplates(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string
  ): Observable<{ [key: string]: AuthoringTemplate }> {
    return null;
  }

  @Get("/get/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getAuthoringTemplate(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") wcmPath: string
  ): Observable<AuthoringTemplate> {
    return null;
  }

  @Put("/lock/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public lockAuthoringTemplate(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<AuthoringTemplate> {
    return null;
  }

  @Delete("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public purgeAuthoringTemplate(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }
}
