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

import { ValidationRule } from "../model/ValidationRule";

@Injectable({
  providedIn: "root"
})
@Client({
  serviceId: "wcm",
  baseUrl: "/wcm/api/validationRule",
  headers: {
    "content-type": "application/json"
  }
})
export class ValidationRuleService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public loadValidationRules(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("filter") filter: string = "",
    @Query("sort") sortDirection: string = "asc",
    @Query("pageIndex") pageIndex: number = 0,
    @Query("pageSize") pageSize: number = 3
  ): Observable<ValidationRule[]> {
    return null;
  }

  // @Get("/{repository}/{workspace}")
  // @Timeout(2000) //In milliseconds
  // @Produces(MediaType.JSON)
  // public getValidationRule(
  //   @Path("repository") repository: string,
  //   @Path("workspace") workspace: string,
  //   @Query("path") wcmPath: string
  // ): Observable<ValidationRule> {
  //   return null;
  // }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createValidationRule(
    @Body validationRule: ValidationRule
  ): Observable<any> {
    return null;
  }

  @Put("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public saveValidationRule(
    @Body validationRule: ValidationRule
  ): Observable<any> {
    return null;
  }

  @Delete("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public purgeValidationRule(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }
}
