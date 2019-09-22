/*
 * Public API Surface of bpw-auth
 */

export * from './lib/authentication/authentication.module';
export * from './lib/authentication/constants';
export * from './lib/authentication/forgot-password/forgot-password/forgot-password.component';
export * from './lib/authentication/lock-screen/lock-screen/lock-screen.component';
// export * from './lib/authentication/login/login/login.component';
export * from './lib/authentication/mail-confirm/mail-confirm/mail-confirm.component';
export * from './lib/authentication/reset-password/reset-password/reset-password.component';
export * from './lib/authentication/signup/signup/signup.component';

export * from './lib/authentication/service/user.service';
export * from './lib/oauth2/oauth2.module';
export * from './lib/oauth2/redirect-handler/redirect-handler.component';
export * from './lib/oauth2/profile/profile.component';

