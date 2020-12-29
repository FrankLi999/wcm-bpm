import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable } from "rxjs";
import {
  RestClient,
  Body,
  Client,
  Delete,
  Get,
  Path,
  Post,
  Put,
  Timeout,
  Produces,
  Query,
  MediaType,
  ApiConfigService,
} from "bpw-rest-client";
import { Authorization } from "../model/Authorization";
import { AuthorizationCreate } from "../model/AuthorizationCreate";
import { Count } from "../model/Count";
import { Resource } from "../model/Resource";

@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "bpm-authorization-ervice",
  baseUrl: "/camunda/api/engine/authorization",
  headers: {
    "content-type": "application/json",
  },
})
export class AuthorizationService extends RestClient {
  onResourceChanged: BehaviorSubject<Resource>;
  constructor(apiConfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiConfigService);
    this.onResourceChanged = new BehaviorSubject(null);
  }

  @Get("/count")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  getAuthorizationCount(
    @Query("resourceType") resourceType: number
  ): Observable<Count> {
    return null;
  }

  @Get("/")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  getAuthorizations(
    @Query("firstResult") firstResult: number,
    @Query("maxResults") maxResults: number,
    @Query("resourceType") resourceType: number
  ): Observable<Authorization[]> {
    return null;
  }

  @Post("/create")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createAuthorization(
    @Body authorization: AuthorizationCreate
  ): Observable<Authorization> {
    return null;
  }

  @Delete("/{resourceId}")
  @Timeout(20000) //In milliseconds
  public deleteAuthorization(
    @Path("resourceId") resourceId: string
  ): Observable<any> {
    return null;
  }

  @Put("/{resourceId}")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public updateAuthorization(
    @Path("resourceId") resourceId: string,
    @Body authorization: Authorization
  ): Observable<any> {
    return null;
  }
}
