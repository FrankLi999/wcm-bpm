import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { UIConfigService } from '../../../../common/services/config.service';
import { wcmAnimations } from '../../../../common/animations/wcm-animations';
import { authLayoutConfig } from '../../../config/auth.config';
import * as authStoreSelector from '../../../store/selectors/auth.selectors';
import * as authStoreAction from '../../../store/actions/auth.actions';
import * as authStoreReducer from '../../../store/reducers/auth.reducers';
import { takeUntil, filter } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { ApiConfigService } from 'bpw-rest-client';

declare var appConfig: any;
@Component({
    selector     : 'login',
    templateUrl  : './login.component.html',
    styleUrls    : ['./login.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : wcmAnimations
})
export class LoginComponent implements OnInit, OnDestroy {
    loginForm: FormGroup;
    googleLogo = 'assets/images/social-login/google-logo.png';
    googleLogin = '';
    fbLogo = 'assets/images/social-login/fb-logo.png';
    facebookLogin = '';
    githubLogo = 'assets/images/social-login/github-logo.png';
    githubLogin: string = '';
    loginError: string = '';
    private unsubscribeAll: Subject<any>;
    
    /**
     * @param _uiConfigService 
     * @param _formBuilder 
     * @param apiConfigService 
     * @param ApiConfigService 
     * @param router 
     * @param store 
     */
    constructor(
        private _uiConfigService: UIConfigService,
        private _formBuilder: FormBuilder,
        private apiConfigService: ApiConfigService,
        //private http: HttpClient,
        private router: Router,
        private store: Store<authStoreReducer.AuthState>
    )
    {
        this.unsubscribeAll = new Subject<any>();
        // Configure the layout
        this._uiConfigService.config = {
            ...authLayoutConfig
        };
    }

    // -----------------------------------------------------------------------------------------------------
    // @ Lifecycle hooks
    // -----------------------------------------------------------------------------------------------------

    /**
      * On init
      */
    ngOnInit(): void {
        this.googleLogin = this.apiConfigService.apiConfig.googleAuthUrl;
        this.facebookLogin = this.apiConfigService.apiConfig.facebookAuthUrl;
        this.githubLogin = this.apiConfigService.apiConfig.githubAuthUrl;

        this.loginForm = this._formBuilder.group({
            email   : ['', [Validators.required, Validators.email]],
            password: ['', Validators.required]
        });

        this.store.pipe(
            select(authStoreSelector.getLoginError),
            filter(loginError => !!loginError),
            takeUntil(this.unsubscribeAll)       
          ).subscribe(
            (loginError: string) => {
            if (loginError) {
                this.loginError = loginError;
            }
        });
        this.store.pipe(
            select(authStoreSelector.isLoggedIn),
            filter(loggedIn => loggedIn),
            takeUntil(this.unsubscribeAll))
            // flatMap(loggedIn => this.store.select(getRouteSnapshot)))
          .subscribe(
              (loggedIn) => {
                // this.router.navigateByUrl('/oauth2/profile');
                this.router.navigateByUrl(appConfig && appConfig.defaultUrl ? appConfig.defaultUrl : '/oauth2/profile');
              }
            // (routeSnapshotOrError: RouteSnapshot) => {
            //     let redirectUrl = (routeSnapshotOrError as RouteSnapshot).paramMap['url'];
            //     redirectUrl = redirectUrl || '/oauth2/profile';
            //     this.router.navigateByUrl(redirectUrl);
            // }
        );
    }

    /**
     * On destroy
     */
    ngOnDestroy(): void {
        this.unsubscribeAll.next();
        this.unsubscribeAll.complete();
        this.loginError && this.store.dispatch(new authStoreAction.LoginClearErrorAction());
    }
    
    handleLogin() {
        this.store.dispatch(new authStoreAction.LoginAction({email: this.loginForm.value.email, password: this.loginForm.value.password}));
        
    }
}
