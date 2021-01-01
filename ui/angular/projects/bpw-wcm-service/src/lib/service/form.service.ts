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
import { Form } from "../model/Form";
@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "wcm",
  baseUrl: "/wcm/api/form",
  headers: {
    "content-type": "application/json",
  },
})
export class FormService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public createForm(@Body form: Form): Observable<any> {
    return null;
  }

  @Put("")
  @Timeout(20000) //In milliseconds
  @Produces(MediaType.JSON)
  public saveForm(@Body form: Form): Observable<any> {
    return null;
  }

  @Get("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getForms(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string
  ): Observable<{ [key: string]: Form }> {
    return null;
  }

  @Get("/get/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getForm(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") wcmPath: string
  ): Observable<Form> {
    return null;
  }

  @Delete("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public purgeForm(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }
}
