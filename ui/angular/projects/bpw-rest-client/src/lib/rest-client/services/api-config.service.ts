import { Inject, Injectable, InjectionToken } from '@angular/core';
import { ApiConfig } from '../types/api-config';
// Create the injection token for the custom settings
export const API_CONFIG = new InjectionToken('apiConfig');

@Injectable({
  providedIn: 'root'
})
export class ApiConfigService {
    // Private
    private readonly _apiConfig: ApiConfig;

    /**
     * Constructor
     *
     * @param _config
     */
    constructor(
        @Inject(API_CONFIG) private _config: ApiConfig
    ) {
        // Set the default config from the user provided config (from forRoot)
        this._apiConfig = _config;
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Accessors
    // -----------------------------------------------------------------------------------------------------

    /**
     * Get default config
     *
     * @returns apiConfig
     */
    get apiConfig(): ApiConfig {
        return this._apiConfig;
    }
}

