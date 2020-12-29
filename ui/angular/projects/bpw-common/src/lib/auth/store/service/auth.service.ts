import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import {
  ApiConfigService,
  Get,
  RestClient,
  Client,
  Path,
  Produces,
  Post,
  Query,
  Timeout,
  Body,
  MediaType,
} from "bpw-rest-client";

import { UserProfile } from "../model/user-profile.model";
import { Login } from "../model/login.model";
import { User } from "../model/user.model";
import { ResetPassword } from "../model/reset-password.model";

@Injectable({
  providedIn: "root",
})
@Client({
  serviceId: "auth-service",
  baseUrl: "/core/api",
  headers: {
    "content-type": "application/json",
  },
})
export class AuthService extends RestClient {
  constructor(apiconfigService: ApiConfigService, httpClient: HttpClient) {
    super(httpClient, apiconfigService);
  }

  @Post("/login")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public login(@Body login: Login): Observable<UserProfile> {
    return null;
  }

  @Get("/user-profile")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getUserProfile(): Observable<UserProfile> {
    return null;
  }

  @Post("/users")
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public signup(@Body user: User): Observable<any> {
    return null;
  }

  @Post("/forgot-password")
  @Timeout(2000) //In milliseconds
  public forgotPassword(@Query("email") email: string): Observable<any> {
    return null;
  }

  @Post("/reset-password")
  @Timeout(2000) //In milliseconds
  public resetPassword(@Body resetPassword: ResetPassword): Observable<any> {
    return null;
  }

  @Post("/users/{id}/verification")
  @Timeout(2000) //In milliseconds
  public verification(
    @Path("id") id: string,
    @Query("code") code: string
  ): Observable<any> {
    return null;
  }

  @Post("/users/{id}/resend-verification-mail")
  @Timeout(2000) //In milliseconds
  public resendVerification(@Path("id") id: string): Observable<any> {
    return null;
  }
}
