import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from "@angular/common/http";
import { Router } from '@angular/router';
import { FuseConfigService } from 'bpw-components';
import { fuseAnimations } from 'bpw-components';
import { GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL, GITHUB_AUTH_URL, API_BASE_URL, ACCESS_TOKEN } from '../../constants';
@Component({
    selector     : 'login',
    templateUrl  : './login.component.html',
    styleUrls    : ['./login.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations   : fuseAnimations
})
export class LoginComponent implements OnInit
{
    loginForm: FormGroup;
    googleLogo = 'assets/images/social-login/google-logo.png';
    googleLogin = GOOGLE_AUTH_URL;
    fbLogo = 'assets/images/social-login/fb-logo.png';
    facebookLogin = FACEBOOK_AUTH_URL;
    githubLogo = 'assets/images/social-login/github-logo.png';
    githubLogin = GITHUB_AUTH_URL;

    /**
     * Constructor
     *
     * @ param {FuseConfigService} _fuseConfigService
     * @ param {FormBuilder} _formBuilder
     */
    constructor(
        private _fuseConfigService: FuseConfigService,
        private _formBuilder: FormBuilder,
        private http: HttpClient,
        private router: Router
    )
    {
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
    ngOnInit(): void
    {
        this.loginForm = this._formBuilder.group({
            email   : ['', [Validators.required, Validators.email]],
            password: ['', Validators.required]
        });
    }

    handleLogin() {
        const loginRequest = Object.assign({}, this.loginForm.value);
        console.log(loginRequest);
        const loginUrl = `${API_BASE_URL}/auth/login`;
        this.http
            .post(loginUrl, loginRequest)
            .subscribe(
              (val: any) => {
                console.log("POST call successful value returned in body", val);
                localStorage.setItem(ACCESS_TOKEN, val.accessToken);
                this.router.navigateByUrl('/oauth2/profile');
              },
              response => {
                console.log("POST call in error", response);
              },
              () => {
                console.log("The POST observable is now completed.");
              });

    }
}
