/*
 * Public API Surface of bpw-auth
 */

export * from './lib/components/authentication.module';
// export * from './lib/components/login/login.module';
// export * from './lib/components/forgot-password/forgot-password.module';
// export * from './lib/components/mail-confirm/mail-confirm.module';
// export * from './lib/components/lock-screen/lock-screen.module';
// export * from './lib/components/reset-password/reset-password.module';
// export * from './lib/components/signup/signup.module';
export * from './lib/components/oauth2.module';
// export * from './lib/components/oauth2-profile/oauth2-profile.module';
// export * from './lib/components/oauth2-redirect-handler/oauth2-redirect-handler.module';
export * from './lib/components/forgot-password/forgot-password/forgot-password.component';
export * from './lib/components/lock-screen/lock-screen/lock-screen.component';
export * from './lib/components/login/login/login.component';
export * from './lib/components/mail-confirm/mail-confirm/mail-confirm.component';
export * from './lib/components/reset-password/reset-password/reset-password.component';
export * from './lib/components/signup/signup/signup.component';

export * from './lib/components/oauth2-redirect-handler/redirect-handler.component';
export * from './lib/components/oauth2-profile/profile.component';
export * from './lib/config';