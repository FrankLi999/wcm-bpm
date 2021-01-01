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
import { Library } from "../model/Library";
@Injectable({
  providedIn: "root"
})
@Client({
  serviceId: "wcm",
  baseUrl: "/wcm/api/library",
  headers: {
    "content-type": "application/json"
  }
})
export class LibraryService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }

  @Post("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createLibrary(@Body library: Library): Observable<any> {
    return null;
  }

  @Put("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public saveLibrary(@Body library: Library): Observable<any> {
    return null;
  }

  @Delete("")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public deleteLibrary(@Body library: Library): Observable<any> {
    return null;
  }

  @Get("/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getLibraries(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("filter") filter: string = "",
    @Query("sort") sortDirection: string = "asc",
    @Query("pageIndex") pageIndex: number = 0,
    @Query("pageSize") pageSize: number = 3
  ): Observable<Library[]> {
    return null;
  }
}
