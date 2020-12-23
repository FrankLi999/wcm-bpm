import ForgotPasswordPageConfig from 'bpw-auth/pages/forgot-password/ForgotPasswordPageConfig';
import LockPageConfig from 'bpw-auth/pages/lock/LockPageConfig';
import MailConfirmPageConfig from 'bpw-auth/pages/mail-confirm/MailConfirmPageConfig';
import RegisterPageConfig from 'bpw-auth/pages/register/RegisterPageConfig';
import ResetPasswordPageConfig from 'bpw-auth/pages/reset-password/ResetPasswordPageConfig';
import LoginConfig from '../pages/auth/login/LoginConfig';

const authPagesConfigs = [
  RegisterPageConfig,
  ResetPasswordPageConfig,
  ForgotPasswordPageConfig,
  MailConfirmPageConfig,
  LockPageConfig,
  LoginConfig
];

export default authPagesConfigs;
