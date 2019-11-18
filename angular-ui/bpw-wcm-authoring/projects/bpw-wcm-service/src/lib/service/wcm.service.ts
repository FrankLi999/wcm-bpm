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
  Query,
  MediaType,
  ApiConfigService
} from 'bpw-rest-client';

import { 
  JsonForm,
  Theme,
  ControlField,
  AuthoringTemplate,
  RenderTemplate,
  ContentAreaLayout,
  SiteArea,
  ContentItem,
  WcmSystem,
  SiteConfig,
  WcmNode,
  WcmItemFilter
} from '../model';
@Injectable({
  providedIn: 'root'
})
@Client({
  serviceId: 'modeshape-service',
  baseUrl: '/wcm/api',
  headers: {
      'content-type': 'application/json'
  }
})
export class WcmService extends RestClient {

  constructor(
    apiConfigService: ApiConfigService,
    httpClient: HttpClient){
    super(httpClient, apiConfigService);
  }

  @Get('/theme/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getTheme(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string
  ): Observable<Theme[]> { return null; };

  @Get('/control/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getControlField(
      @Path('repository') repository: string, 
      @Path('workspace') workspace: string): Observable<ControlField[]> { return null; };
  

  @Post('/at')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createAuthoringTemplate(@Body at: AuthoringTemplate): Observable<any> { return null; };
  
  @Get('/at/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getAuthoringTemplates(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string
  ): Observable<AuthoringTemplate[]> { return null; };

  @Get('/authoringTemplate/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getAuthoringTemplate(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string,
    @Query('path') nodePath: string
  ): Observable<AuthoringTemplate> { return null; };

  @Get('/wcmSystem/{repository}/{workspace}/{library}/{siteConfig}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getWcmSystem(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string,
    @Path('library') library: string, 
    @Path('siteConfig') siteConfig: string
  ): Observable<WcmSystem> { return null; };

  @Get('/jsonform/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getJsonForms(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string
  ): Observable<{[key:string]:JsonForm}> { return null; };

  @Post('/rt')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createRenderTemplate(@Body rt: RenderTemplate): Observable<any> { return null; };

  @Post('/siteConfig')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createSiteConfig(@Body siteConfig: SiteConfig): Observable<any> { return null; };

  @Get('/rt/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getRenderTemplates(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string
  ): Observable<{[key:string]:RenderTemplate}> { return null; };

  @Get('/renderTemplate/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getRenderTemplate(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string,
    @Query('path') renderTemplatePath: string,
  ): Observable<RenderTemplate> { return null; };
  
  @Post('/contentAreaLayout')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createContentAreaLayout(@Body contentAreaLayout: ContentAreaLayout): Observable<any> { return null; };

  @Get('/contentAreaLayout/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public geContentAreaLayouts(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string
  ): Observable<{[key:string]:ContentAreaLayout}> { return null; };

  @Post('/sitearea')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createSiteArea(@Body sa: SiteArea): Observable<any> { return null; };

  @Get('/siteArea/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getSiteArea(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string,
    @Query("path") contentItemPath: string
  ): Observable<SiteArea> { return null; };

  @Post('/ContentItem')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public createContentItem(@Body contentItem: ContentItem): Observable<any> { return null; };

  @Get('/contentItem/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  public getContentItem(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string,
    @Query("path") contentItemPath: string
    //@Path('contentItemPath') ontentItemPath: string
  ): Observable<ContentItem> { return null; };

  @Post('/wcmNodes/{repository}/{workspace}')
  @Timeout(2000) //In milliseconds
  @Produces(MediaType.JSON)
  getWcmNodes(
    @Path('repository') repository: string, 
    @Path('workspace') workspace: string,
    @Body filter: WcmItemFilter): Observable<WcmNode[]> { return null; };
}
