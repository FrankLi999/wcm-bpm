import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ACCESS_TOKEN } from '../../authentication/constants';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
    //Do thing in ApplicationComponent.ngOnInit
    // getCurrentUser()
    // .then(response => {
    //   this.setState({
    //     currentUser: response,
    //     authenticated: true,
    //     loading: false
    //   });
    // }).catch(error => {
    //   this.setState({
    //     loading: false
    //   });  
    // });


    // export function getCurrentUser() {
    //   if(!localStorage.getItem(ACCESS_TOKEN)) {
    //       return Promise.reject("No access token set.");
    //   }
  
    //   return request({
    //       url: API_BASE_URL + "/user/me",
    //       method: 'GET'
    //   });
    // }
  }

  handleLogout() {
    localStorage.removeItem(ACCESS_TOKEN);
    // this.setState({
    //   authenticated: false,
    //   currentUser: null
    // });
    this.router.navigateByUrl('/auth/login');
  }
}
