import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ACCESS_TOKEN } from '../../authentication/constants';
@Component({
  selector: 'redirect-handler',
  templateUrl: './redirect-handler.component.html',
  styleUrls: ['./redirect-handler.component.scss']
})
export class RedirectHandlerComponent implements OnInit {

  constructor(private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    const token = this.route.snapshot.queryParams['token'];
    const error = this.route.snapshot.queryParams['error'];
    //this.activatedRoute.queryParams.subscribe(params => {
    //const token = params['token'];
    //const error = params['error'];
    console.log(`token: ${token}`);
    console.log(`error: ${error}`);

    if (token) {
      localStorage.setItem(ACCESS_TOKEN, token);
      this.router.navigateByUrl('/oath2/profile');
    } else {
      this.router.navigateByUrl('/auth/login');    
    }
    //});
  }

}
