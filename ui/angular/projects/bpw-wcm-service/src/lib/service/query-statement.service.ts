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
  ApiConfigService,
} from "bpw-rest-client";
import { QueryStatement } from "../model/QueryStatement";
import { QueryResult } from "../model/QueryResult";

@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "modeshape-service",
  baseUrl: "/wcm/api/queryStatement",
  headers: {
    "content-type": "application/json",
  },
})
export class QueryStatementService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Get("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public loadQueryStatements(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("filter") filter: string = "",
    @Query("sort") sortDirection: string = "asc",
    @Query("pageIndex") pageIndex: number = 0,
    @Query("pageSize") pageSize: number = 3
  ): Observable<QueryStatement[]> {
    return null;
  }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createQueryStatement(
    @Body queryStatement: QueryStatement
  ): Observable<any> {
    return null;
  }

  @Put("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public saveQueryStatement(
    @Body queryStatement: QueryStatement
  ): Observable<any> {
    return null;
  }

  @Post("/query")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public executeQueryStatement(
    @Body queryStatement: QueryStatement
  ): Observable<QueryResult> {
    return null;
  }

  @Delete("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public purgeQuery(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") absPath: string
  ): Observable<any> {
    return null;
  }
}
