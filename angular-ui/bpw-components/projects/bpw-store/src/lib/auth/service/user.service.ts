import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from 'rxjs';

import {
  RestClient, 
  Client, 
  Get,
  Body,
  Timeout,
  Produces,
  MediaType
} from 'bpw-rest-client';

import {API_BASE_URL} from 'bpw-rest-client';
import { AuthModule } from '../auth.module';
import {
  UserProfile,
  Login
} from '../model';

@Injectable({
  providedIn: AuthModule
})
@Client({
  serviceId: 'user-service',
  baseUrl: `${API_BASE_URL}/user/api/rest`,
  headers: {
      'content-type': 'application/json'
  }
})
export class userService extends RestClient {

  constructor(httpClient: HttpClient){
    super(httpClient);
  }

  @Get('/me')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public me(
  ): Observable<UserProfile> { return null; };
}

