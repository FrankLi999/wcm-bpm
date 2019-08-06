import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from 'rxjs';

import {
  RestClient, 
  Client, 
  Get,
  Post,
  Body,
  Path,
  Timeout,
  Produces, 
  MediaType
} from '../../rest-client';

import {API_BASE_URL} from '../../authentication/constants';

import { 
  Theme,
  ControlField,
  AuthoringTemplate,
  RenderTemplate,
  PageLayout
} from '../model';
@Injectable()
@Client({
  serviceId: 'modeshape-service',
  baseUrl: `${API_BASE_URL}/wcm/api/rest`,
  headers: {
      'content-type': 'application/json'
  }
})
export class WcmService extends RestClient {

  constructor(httpClient: HttpClient){
    super(httpClient);
  }

  @Get('/theme')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getTheme(): Observable<Theme[]> { return null; };

  @Get('/{repository}/{workspace}/control')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getControlField(
      @Path('repository') repository: string, 
      @Path('workspace') workspace: string): Observable<ControlField[]> { return null; };
  

  @Post('/at')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createAuthoringTemplate(@Body at: AuthoringTemplate): Observable<any> { return null; };
  
  @Get('/at')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getAuthoringTemplate(): Observable<AuthoringTemplate[]> { return null; };

  @Post('/rt')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createRenderTemplate(@Body rt: RenderTemplate): Observable<any> { return null; };

  @Get('/rt')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getRenderTemplate(): Observable<RenderTemplate[]> { return null; };

  
  @Post('/pagelayout')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createPagelayout(@Body rt: PageLayout): Observable<any> { return null; };

  @Get('/pagelayout')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getPagelayout(): Observable<PageLayout[]> { return null; };
}
