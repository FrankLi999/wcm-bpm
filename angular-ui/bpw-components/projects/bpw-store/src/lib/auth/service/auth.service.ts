import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from 'rxjs';

import {
  RestClient, 
  Client, 
  Post,
  Body,
  Timeout,
  Produces,
  MediaType,
  ApiConfigService
} from 'bpw-rest-client';

import {
  UserProfile,
  Login
} from '../model';

@Injectable({ 
    providedIn: 'root' 
})
@Client({
  serviceId: 'auth-service',
  baseUrl: '/auth/api/rest',
  headers: {
      'content-type': 'application/json'
  }
})
export class AuthService extends RestClient {

  constructor(
    apiconfigService: ApiConfigService,
    httpClient: HttpClient){
    super(httpClient, apiconfigService);
  }

  @Post('/login')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public login(
    @Body login: Login
  ): Observable<UserProfile> { return null; };

  // @Post('/logout')
  // @Timeout(2000) //In milliseconds
  // @Produces(MediaType.JSON)
  // public logout(
  // ): Observable<void> { return null; };
}