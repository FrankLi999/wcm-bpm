import ForgotPasswordPageConfig from './forgot-password/ForgotPasswordPageConfig';
import LockPageConfig from './lock/LockPageConfig';
import MailConfirmPageConfig from './mail-confirm/MailConfirmPageConfig';
import RegisterPageConfig from './register/RegisterPageConfig';
import ResetPasswordPageConfig from './reset-password/ResetPasswordPageConfig';
import LoginConfig from './login/LoginConfig';
import LogoutConfig from './logout/LogoutConfig';

const authPagesConfigs = [
  RegisterPageConfig,
  ResetPasswordPageConfig,
  ForgotPasswordPageConfig,
  MailConfirmPageConfig,
  LockPageConfig,
  LoginConfig,
  LogoutConfig,
];

export default authPagesConfigs;
