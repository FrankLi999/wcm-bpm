import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from "@angular/common/http";
import { Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { FuseConfigService } from 'bpw-components';
import { fuseAnimations } from 'bpw-components';
import { takeUntil, flatMap, filter } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { ApiConfigService } from 'bpw-rest-client';
import * as fromStore from 'bpw-store';

@Component({
    selector     : 'login',
    templateUrl  : './login.component.html',
    styleUrls    : ['./login.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : fuseAnimations
})
export class LoginComponent implements OnInit, OnDestroy
{
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
     * @param _fuseConfigService 
     * @param _formBuilder 
     * @param apiConfigService 
     * @param ApiConfigService 
     * @param router 
     * @param store 
     */
    constructor(
        private _fuseConfigService: FuseConfigService,
        private _formBuilder: FormBuilder,
        private apiConfigService: ApiConfigService,
        //private http: HttpClient,
        private router: Router,
        private store: Store<fromStore.AuthState>
    )
    {
        this.unsubscribeAll = new Subject<any>();
        // Configure the layout
        this._fuseConfigService.config = {
            layout: {
                navbar   : {
                    hidden: true
                },
                toolbar  : {
                    hidden: true
                },
                footer   : {
                    hidden: true
                },
                sidepanel: {
                    hidden: true
                }
            }
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
            select(fromStore.getLoginError),
            filter(loginError => !!loginError),
            takeUntil(this.unsubscribeAll)       
          ).subscribe(
            (loginError: string) => {
            if (loginError) {
                this.loginError = loginError;
            }
        });
        this.store.pipe(
            select(fromStore.isLoggedIn),
            filter(loggedIn => loggedIn),
            takeUntil(this.unsubscribeAll))
            // flatMap(loggedIn => this.store.select(getRouteSnapshot)))
          .subscribe(
              (loggedIn) => {
                this.router.navigateByUrl('/oauth2/profile');
              }
            // (routeSnapshotOrError: RouteSnapshot) => {
            //     console.log(">>>>>>>>>>>>>>> logged in already");
            //     let redirectUrl = (routeSnapshotOrError as RouteSnapshot).paramMap['url'];
            //     console.log(">>>>>>>>>>>>>>> redirectUrl:" + redirectUrl);
            //     redirectUrl = redirectUrl || '/oauth2/profile';
            //     console.log(">>>>>>>>>>>>>>> redirectUrl2:" + redirectUrl);
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
        this.loginError && this.store.dispatch(new fromStore.LoginClearErrorAction());
    }
    
    handleLogin() {
        this.store.dispatch(new fromStore.LoginAction({email: this.loginForm.value.email, password: this.loginForm.value.password}));
        
    }
}
