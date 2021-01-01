import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {
  RestClient,
  Client,
  Get,
  Put,
  Body,
  Path,
  Timeout,
  Produces,
  Query,
  MediaType,
  ApiConfigService
} from "bpw-rest-client";

import { Grant } from "../model/Grant";
@Injectable({
  providedIn: "root"
})
@Client({
  serviceId: "wcm",
  baseUrl: "/wcm/api/acl",
  headers: {
    "content-type": "application/json"
  }
})
export class AclService extends RestClient {
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
  }
  @Get("/groups")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getGroups(
    @Query("filter") filter: string = "",
    @Query("sort") sortDirection: string = "asc",
    @Query("pageIndex") pageIndex: number = 0,
    @Query("pageSize") pageSize: number = 3
  ): Observable<string[]> {
    return null;
  }

  @Get("/users")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getUsers(
    @Query("filter") filter: string = "",
    @Query("sort") sortDirection: string = "asc",
    @Query("pageIndex") pageIndex: number = 0,
    @Query("pageSize") pageSize: number = 3
  ): Observable<string[]> {
    return null;
  }

  @Get("/grant/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getGrants(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") wcmPath: string = ""
  ): Observable<Grant> {
    return null;
  }

  @Get("/library-grant/{repository}/{workspace}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getLibraryGrants(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Query("path") wcmPath: string = ""
  ): Observable<{ [key: string]: Grant }> {
    return null;
  }

  @Put("/grant/{repository}/{workspace}")
  @Produces(MediaType.JSON)
  public updateGrants(
    @Path("repository") repository: string,
    @Path("workspace") workspace: string,
    @Body grant: Grant
  ): Observable<any> {
    return null;
  }
}
