//import { bpwDark, skyBlue } from 'bpw-common/colors';
import { blueGrey, grey, red } from '@material-ui/core/colors';
const themesConfig = {
  default: {
    palette: {
      type: 'light',
      primary: blueGrey,
      secondary: {
        light: grey[100],
        main: grey[300],
        dark: grey[500],
      },
      background: {
        paper: '#FFFFFF',
        default: '#f6f7f9',
      },
      error: red,
    },
    status: {
      danger: 'orange',
    },
  },
};

export default themesConfig;
