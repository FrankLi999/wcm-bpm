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
  ApplicationConfig,
  Theme,
  ControlField,
  AuthoringTemplate,
  RenderTemplate,
  ContentAreaLayout,
  SiteArea,
  ContentItem,
  SiteConfig
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

  @Get('/{repository}/{workspace}/theme')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getTheme(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string
  ): Observable<Theme[]> { return null; };

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
  
  @Get('/{repository}/{workspace}/at')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getAuthoringTemplate(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string
  ): Observable<AuthoringTemplate[]> { return null; };

  @Get('/{repository}/{workspace}/applicationConfig/{library}/{siteConfig}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getApplicationConfig(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string,
    @Path('repository') library: string, 
    @Path('workspace') siteConfig: string
  ): Observable<ApplicationConfig> { return null; };

  @Post('/rt')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createRenderTemplate(@Body rt: RenderTemplate): Observable<any> { return null; };

  @Post('/siteConfig')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createSiteConfig(@Body siteConfig: SiteConfig): Observable<any> { return null; };

  @Get('/{repository}/{workspace}/rt')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getRenderTemplate(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string
  ): Observable<RenderTemplate[]> { return null; };

  
  @Post('/contentAreaLayout')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createContentAreaLayout(@Body contentAreaLayout: ContentAreaLayout): Observable<any> { return null; };

  @Get('/{repository}/{workspace}/contentAreaLayout')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public geContentAreaLayout(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string
  ): Observable<ContentAreaLayout[]> { return null; };

  @Post('/sitearea')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createSiteArea(@Body sa: SiteArea): Observable<any> { return null; };

  // @Post('/page')
  // @Timeout(2000) //In milliseconds
  // @Produces(MediaType.JSON)
  // public createPage(@Body page: Page): Observable<any> { return null; };

  @Post('/ContentItem')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createContentItem(@Body contentItem: ContentItem): Observable<any> { return null; };
}
