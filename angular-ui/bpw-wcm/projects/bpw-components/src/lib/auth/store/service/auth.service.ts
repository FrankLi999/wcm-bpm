import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from 'rxjs';

import {   
  ApiConfigService,
  RestClient,
  Client,
  Produces,
  Post,
  Timeout,
  Body,
  MediaType,
} from 'bpw-rest-client';

import { UserProfile } from '../model/user-profile.model';
import { Login } from '../model/login.model';

@Injectable({ 
    providedIn: 'root' 
})
@Client({
  serviceId: 'auth-service',
  baseUrl: '/auth/api',
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