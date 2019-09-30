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
  MediaType
} from 'bpw-rest-client';

import { API_BASE_URL } from 'bpw-rest-client';
import { AuthModule } from '../auth.module';
import {
  UserProfile,
  Login
} from '../model';

@Injectable({
  providedIn: AuthModule
})
@Client({
  serviceId: 'auth-service',
  baseUrl: `${API_BASE_URL}/auth/api/rest`,
  headers: {
      'content-type': 'application/json'
  }
})
export class AuthService extends RestClient {

  constructor(httpClient: HttpClient){
    super(httpClient);
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