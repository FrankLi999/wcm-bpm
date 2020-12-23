import React, { useState } from 'react';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import clsx from 'clsx';
import { Link } from 'react-router-dom';
import Divider from '@material-ui/core/Divider';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import InputAdornment from '@material-ui/core/InputAdornment';
import Typography from '@material-ui/core/Typography';
import { darken } from '@material-ui/core/styles/colorManipulator';
import Checkbox from '@material-ui/core/Checkbox';
import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useDispatch } from 'react-redux';
import BpwAnimate from 'bpw-common/elements/BpwAnimate';
import { Formik, ErrorMessage } from 'formik';
import { TextField, Button, makeStyles } from '@material-ui/core';
import * as Yup from 'yup';
import { submitLogin } from '../../store/loginSlice';
const useStyles = makeStyles((theme) => ({
  root: {
    background: `linear-gradient(to right, ${theme.palette.primary.dark} 0%, ${darken(theme.palette.primary.dark, 0.5)} 100%)`,
    color: theme.palette.primary.contrastText,
  },
}));

function LoginPage(props) {
  const dispatch = useDispatch();
  // const login = useSelector(({ auth }) => auth.login);
  // const formRef = useRef(null);

  // useEffect(() => {
  //   if (login.error && (login.error.email || login.error.password)) {
  //     formRef.current.updateInputsWithError({
  //       ...login.error,
  //     });
  //     disableButton();
  //   }
  // }, [login.error]);

  const classes = useStyles();
  const [showPassword, setShowPassword] = useState(false);

  function handleSubmit(model) {
    dispatch(submitLogin({ email: model.email, password: model.password, loadCustomeData: props.loadCustomeData }));
  }

  const googleLogin = () => {
    window.location = 'http://wcm-server.bpwizard.com:28080/oauth2/authorization/google?redirect_uri=http://gateway-ui:5009/oauth2/redirect';
  };

  const facebookLogin = () => {
    window.location = 'http://wcm-server.bpwizard.com:28080/oauth2/authorization/facebook?redirect_uri=http://gateway-ui:5009/oauth2/redirect';
  };

  const githubLogin = () => {
    window.location = 'http://wcm-server.bpwizard.com:28080/oauth2/authorization/github?redirect_uri=http://gateway-ui:5009/oauth2/redirect';
  };

  return (
    <div className={clsx(classes.root, 'flex flex-col flex-auto flex-shrink-0 p-24 md:flex-row md:p-0')}>
      <div
        style={{ flex: '1 1', textAlign: 'left' }}
        className="flex flex-col flex-grow-0 items-center text-white p-16 text-center md:p-128 md:items-start md:flex-shrink-0 md:flex-1 md:text-left"
      >
        <BpwAnimate animation="transition.expandIn">
          <img className="w-128 mb-32" src="/assets/images/logos/app.svg" alt="logo" />
        </BpwAnimate>

        <BpwAnimate animation="transition.slideUpIn" delay={300}>
          <Typography variant="h3" color="inherit" className="font-light">
            Welcome to the API Gateway UI!
          </Typography>
        </BpwAnimate>

        <BpwAnimate delay={400}>
          <Typography variant="subtitle1" color="inherit" className="max-w-512 mt-16">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ullamcorper nisl erat, vel convallis elit fermentum pellentesque. Sed mollis velit
            facilisis facilisis.
          </Typography>
        </BpwAnimate>
      </div>

      <BpwAnimate animation={{ translateX: [0, '100%'] }}>
        <Card className="w-full max-w-400 mx-auto m-16 md:m-0" square>
          <CardContent className="flex flex-col items-center justify-center p-32 md:p-48 md:pt-128 ">
            <Typography variant="h6" className="md:w-full mb-32">
              LOGIN TO YOUR ACCOUNT
            </Typography>

            <Formik
              initialValues={{
                email: '',
                password: '',
                remember: false,
              }}
              onSubmit={handleSubmit}
              validationSchema={Yup.object().shape({
                email: Yup.string()
                  //.email()
                  .required('Enter valid email-id')
                  .min(6, 'Min character length is 6'),
                password: Yup.string()
                  //.matches(/^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()]).{8,20}\S$/)
                  .required('Please valid password. One uppercase, one lowercase, one special character and no spaces')
                  .min(4, 'Min character length is 4'),
              })}
            >
              {(props) => {
                const { values, touched, errors, isValid, isSubmitting, handleChange, handleBlur, handleSubmit } = props;
                return (
                  <form className="flex flex-col justify-center w-full" onSubmit={handleSubmit}>
                    <TextField
                      className="mb-16"
                      type="text"
                      name="email"
                      label="Username/Email"
                      value={values.email}
                      onChange={handleChange}
                      onBlur={handleBlur}
                      error={errors.email && touched.email}
                      helperText={touched.email && errors.email}
                      InputProps={{
                        endAdornment: (
                          <InputAdornment position="end">
                            <Icon className="text-20" color="action">
                              email
                            </Icon>
                          </InputAdornment>
                        ),
                      }}
                      variant="outlined"
                      fullWidth
                    />
                    <TextField
                      className="mb-16"
                      type="password"
                      name="password"
                      label="Password"
                      value={values.password}
                      onChange={handleChange}
                      onBlur={handleBlur}
                      error={errors.password && touched.password}
                      helperText={touched.password && errors.password}
                      InputProps={{
                        className: 'pr-2',
                        type: showPassword ? 'text' : 'password',
                        endAdornment: (
                          <InputAdornment position="end">
                            <IconButton className="m-0 p-0" onClick={() => setShowPassword(!showPassword)}>
                              <Icon className="text-20" color="action">
                                {showPassword ? 'visibility' : 'visibility_off'}
                              </Icon>
                            </IconButton>
                          </InputAdornment>
                        ),
                      }}
                      variant="outlined"
                      fullWidth
                    />
                    <div className="flex items-center justify-between">
                      <FormControl error={errors.remember && touched.remember} className="my-16">
                        <FormControlLabel
                          control={
                            <Checkbox
                              name="remember"
                              value={values.remember}
                              type="checkbox"
                              checked={values.remember}
                              onChange={handleChange}
                              onBlur={handleBlur}
                            />
                          }
                          label="Remember Me"
                        />
                        {errors.remember && touched.remember && (
                          <FormHelperText>
                            <ErrorMessage name="remember" />
                          </FormHelperText>
                        )}
                      </FormControl>
                      <Link className="font-medium" to="/pages/auth/forgot-password">
                        Forgot Password?
                      </Link>
                    </div>
                    <Button
                      type="submit"
                      variant="contained"
                      color="primary"
                      className="w-224 mx-auto mt-16 normal-case"
                      aria-label="LOG IN"
                      disabled={!isValid || isSubmitting}
                      value="legacy"
                    >
                      Login
                    </Button>
                  </form>
                );
              }}
            </Formik>

            <div className="my-24 flex items-center justify-center">
              <Divider className="w-32" />
              <span className="mx-8 font-bold">OR</span>
              <Divider className="w-32" />
            </div>

            <Button onClick={googleLogin} variant="contained" color="secondary" size="small" className="normal-case w-192 mb-8">
              Log in with Google
            </Button>

            <Button onClick={facebookLogin} variant="contained" color="primary" size="small" className="normal-case w-192 mb-8">
              Log in with Facebook
            </Button>

            <Button onClick={githubLogin} variant="contained" color="default" size="small" className="normal-case w-192">
              Log in with Github
            </Button>
            <div className="flex flex-col items-center justify-center pt-32 pb-24">
              <span className="font-medium">Don't have an account?</span>
              <Link className="font-medium" to="/auth/register">
                Create an account
              </Link>
            </div>
          </CardContent>
        </Card>
      </BpwAnimate>
    </div>
  );
}

export default LoginPage;
