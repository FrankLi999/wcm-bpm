import { HttpClient, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiConfigService } from './services/api-config.service';
/**
 * Angular 8 RestClient class.
 *
 * class RestClient
 * constructor
 */
export class RestClient {

  public constructor(
    private httpClient: HttpClient,
    private apiConfigService: ApiConfigService) {
  }

  public getServiceId(): string {
    return null;
  }

  public getBaseUrl(): string {
    return null;
  };

  public getApiBaseUrl(): string {
    return this.apiConfigService.apiConfig.apiBaseUrl;
  };

  public getDefaultHeaders(): Object {
    return null;
  };

  /**
   * Request Interceptor
   *
   * method requestInterceptor
   * param {HttpRequest} req - request object
   */
  protected requestInterceptor(req: HttpRequest<any>):void {
    //
  }

  /**
   * Response Interceptor
   *
   * method responseInterceptor
   * param {HttpResponse} res - response object
   * returns {any} res - transformed response object
   */
  protected responseInterceptor(res: Observable<HttpResponse<any>>): Observable<any> {
    return res;
  }

}
