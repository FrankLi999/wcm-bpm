import { createStyles, makeStyles } from '@material-ui/core';

const useStyles = makeStyles(() =>
  createStyles({
    '@global': {
      html: {
        'font-size': '62.5%',
        'font-family': 'Muli, Roboto, Helvetica Neue, Arial, sans-serif',
        'background-color': '#121212',
      },
      body: {
        'font-size': '14px',
        'line-height': '1.4',
      },
      'html, body, #root': {
        width: '100%',
        height: '100%',
        overflow: 'hidden',
        position: 'relative',
        margin: 0,
      },
      'h1, .h1': {
        'font-size': '24px',
      },
      'h2, .h2': {
        'font-size': '20px',
      },
      'h3, .h3': {
        'font-size': '16px',
      },

      'h4, .h4': {
        'font-size': '15px',
      },
      'h5, .h5': {
        'font-size': '13px',
      },
      'h6, .h6': {
        'font-size': '12px',
      },
      '.ps > .ps__rail-y, .ps > .ps__rail-x': {
        'z-index': 99,
      },
      'a[role=button]': {
        'text-decoration': 'none',
      },
      '[role="tooltip"]': {
        'z-index': 9999,
      },
      '.w-full': {
        width: '100%',
      },
      '.min-w-full': {
        'min-width': '100%',
      },
      '.md\\:w-full': {
        width: '100%',
      },
      '.h-full': {
        height: '100%',
      },
      '.relative': {
        position: 'relative',
      },
      '.flex': {
        display: 'flex',
      },
      '.flex-1': {
        flex: '1',
      },
      '.md\\:flex-1': {
        flex: '1',
      },
      '.flex-col': {
        'flex-direction': 'column',
      },
      '.flex-row': {
        'flex-direction': 'row',
      },
      '.sm\\:flex-row': {
        'flex-direction': 'row',
      },
      '.flex-shrink': {
        'flex-shrink': 1,
      },
      '.flex-shrink-0': {
        'flex-shrink': 0,
      },
      '.flex-grow-0': {
        'flex-grow': 0,
      },
      '.md\\:flex-shrink-0': {
        'flex-shrink': 0,
      },
      '.overflow-hidden': {
        overflow: 'hidden',
      },
      '.flex-auto': {
        flex: '1 1 auto',
      },
      '.items-center': {
        'align-items': 'center',
      },
      '.sm\\:items-center': {
        'align-items': 'center',
      },
      '.justify-center': {
        'justify-content': 'center',
      },
      '.items-start': {
        'align-items': 'flex-start',
      },
      '.justify-between': {
        'justify-content': 'space-between',
      },
      '.justify-strat': {
        'justify-content': 'flex-start',
      },
      '.sm\\:justify-strat': {
        'justify-content': 'flex-start',
      },
      '.md\\:items-start': {
        'align-items': 'flex-start',
      },
      '.md\\:flex-row': {
        'flex-direction': 'row',
      },
      '.md\\:flex-row-reverse': {
        'flex-direction': 'row-reverse',
      },
      '.md\\:flex-col': {
        'flex-direction': 'column',
      },
      '.md\\:flex-col-reverse': {
        'flex-direction': 'column-reverse',
      },
      '.z-0': {
        'z-index': 0,
      },
      '.z-10': {
        'z-index': 10,
      },
      '.z-9999': {
        'z-index': 9999,
      },
      '.text-8': {
        'font-size': '.8rem',
      },
      '.text-10': {
        'font-size': '1.0rem',
      },
      '.text-12': {
        'font-size': '1.2rem',
      },
      '.text-13': {
        'font-size': '1.3rem',
      },
      '.text-14': {
        'font-size': '1.4rem',
      },
      '.text-16': {
        'font-size': '1.6rem',
      },
      '.text-18': {
        'font-size': '1.8rem',
      },
      '.text-96': {
        'font-size': '9.6rem',
      },
      '.font-light': {
        'font-weight': 300,
      },
      '.mx-12': {
        'margin-left': '1.2rem',
        'margin-right': '1.2rem',
      },
      '.mx-14': {
        'margin-left': '1.4rem',
        'margin-right': '1.4rem',
      },
      '.mb-16': {
        'margin-bottom': '1.6rem',
      },
      '.mb-24': {
        'margin-bottom': '2.4rem',
      },
      '.mb-28': {
        'margin-bottom': '2.8rem',
      },
      '.mt-8': {
        'margin-top': '.8rem',
      },
      '.mt-12': {
        'margin-top': '1.2rem',
      },
      '.mt-16': {
        'margin-top': '1.6rem',
      },
      '.mt-32': {
        'margin-top': '3.2rem',
      },
      '.mt-48': {
        'margin-top': '4.8rem',
      },
      '.mr-8': {
        'margin-right': '0.8rem',
      },
      '.mx-16': {
        'margin-left': '1.6rem',
        'margin-right': '1.6rem',
      },
      '.mx-20': {
        'margin-left': '2rem',
        'margin-right': '2rem',
      },
      '.mx-24': {
        'margin-left': '2.4rem',
        'margin-right': '2.4rem',
      },
      '.my-24': {
        'margin-top': '2.4rem',
        'margin-bottom': '2.4rem',
      },
      '.lg\\:mx-24': {
        'margin-left': '2.4rem',
        'margin-right': '2.4rem',
      },
      '.w-24': {
        width: '2.4rem',
      },
      '.w-32': {
        width: '3.2rem',
      },
      '.w-48': {
        width: '4.8rem',
      },
      '.w-72': {
        width: '7.2rem',
      },
      '.w-128': {
        width: '12.8rem',
      },
      '.w-192': {
        width: '19.2rem',
      },
      '.w-224': {
        width: '22.4rem',
      },
      '.min-w-20': {
        'min-width': '2rem',
      },
      '.min-w-32': {
        'min-width': '3.2rem',
      },
      '.min-w-40': {
        'min-width': '4rem',
      },
      '.max-w-384': {
        'max-width': '38.4rem',
      },
      '.max-w-400': {
        'max-width': '40rem',
      },
      '.max-w-512': {
        'max-width': '51.2rem',
      },
      '.max-w-sm': {
        'max-width': '48rem',
      },
      '.max-w-md': {
        'max-width': '64rem',
      },

      '.md\\:min-h-64': {
        'min-height': '6.4rem',
      },
      '.h-20': {
        height: '2rem',
      },
      '.h-24': {
        height: '2.4rem',
      },
      '.h-40': {
        height: '4rem',
      },
      '.h-48': {
        height: '4.8rem',
      },
      '.h-72': {
        height: '7.2rem',
      },
      '.absolute': {
        position: 'absolute',
      },
      '.md\\:h-24': {
        height: '2.4rem',
      },
      '.md\\:p-48': {
        padding: '4.8rem',
      },
      '.md\\:pt-128': {
        'padding-top': '12.8rem',
      },
      '.md\\:p-128': {
        padding: '12.8rem',
      },
      '.p-0': {
        padding: 0,
      },
      '.p-6': {
        padding: '.6rem',
      },
      '.p-12': {
        padding: '1.2rem',
      },
      '.p-16': {
        padding: '1.6rem',
      },
      '.p-24': {
        padding: '2.4rem',
      },
      '.p-32': {
        padding: '3.2rem',
      },
      '.pb-12': {
        'padding-bottom': '1.2rem',
      },
      '.pb-24': {
        'padding-bottom': '2.4rem',
      },
      '.pb-64': {
        'padding-bottom': '6.4rem',
      },
      '.pr-48': {
        'padding-right': '4.8rem',
      },
      '.pt-24': {
        'padding-top': '2.4rem',
      },
      '.pt-32': {
        'padding-top': '3.2rem',
      },
      '.px-0': {
        'padding-left': 0,
        'padding-right': 0,
      },
      '.px-8': {
        'padding-left': '.8rem',
        'padding-right': '.8rem',
      },
      '.px-12': {
        'padding-left': '1.2rem',
        'padding-right': '1.2rem',
      },
      '.px-16': {
        'padding-left': '1.6rem',
        'padding-right': '1.6rem',
      },
      '.px-24': {
        'padding-left': '2.4rem',
        'padding-right': '2.4rem',
      },
      '.py-0': {
        'padding-top': 0,
        'padding-bottom': 0,
      },
      '.py-10': {
        'padding-top': '1.6rem',
        'padding-bottom': '1.6rem',
      },
      '.left-0': {
        left: 0,
      },
      '.right-0': {
        right: 0,
      },
      '.top-0': {
        top: 0,
      },
      "[dir='ltr'] .ltr\\:pr-48,[dir='ltr'].ltr\\:pr-48": {
        'padding-right': '4.8rem',
      },
      "[dir='ltr'] .ltr\\:top-0,[dir='ltr'].ltr\\:top-0": {
        top: 0,
      },
      "[dir='ltr'] .ltr\\:right-0,[dir='ltr'].ltr\\:right-0": {
        right: 0,
      },
      "[dir='ltr'] .ltr\\:bottom-0,[dir='ltr'].ltr:bottom-0": {
        bottom: 0,
      },
      "[dir='ltr'] .ltr\\:left-0,[dir='ltr'].ltr\\:left-0": {
        left: 0,
      },
      "[dir='rtl'] .rtl\\:top-0,[dir='rtl'].rtl\\:top-0": {
        top: 0,
      },
      "[dir='rtl'] .rtl\\:right-0,[dir='rtl'].rtl\\:right-0": {
        right: 0,
      },
      "[dir='rtl'] .rtl\\:bottom-0,[dir='rtl'].rtl\\:bottom-0": {
        bottom: 0,
      },
      "[dir='rtl'] .rtl\\:left-0,[dir='rtl'].rtl\\:left-0": {
        left: 0,
      },
      '.md\\:p-0': {
        padding: 0,
      },
      '.m-0': {
        margin: 0,
      },
      '.mb-8': {
        'margin-bottom': '.8rem',
      },
      '.mb-32': {
        'margin-bottom': '3.2rem',
      },
      '.mx-4': {
        'margin-left': '.4rem',
        'margin-right': '.4rem',
      },
      '.mx-8': {
        'margin-left': '.8rem',
        'margin-right': '.8rem',
      },
      '.-mx-8': {
        'margin-left': '-.8rem',
        'margin-right': '-.8rem',
      },
      '.mx-auto': {
        'margin-left': 'auto',
        'margin-right': 'auto',
      },
      'm-16': {
        margin: '1.6rem',
      },
      '.md\\:m-0': {
        margin: 0,
      },
      '.rounded-4': {
        'border-radius': '.4rem',
      },
      '.rounded-6': {
        'border-radius': '.6rem',
      },
      '.rounded-8': {
        'border-radius': '.8rem',
      },
      '.md\\:text-left': {
        'text-align': 'left',
      },
      '.md\\:text-center': {
        'text-align': 'center',
      },
      '.md\\:text-right': {
        'text-align': 'right',
      },
      '.md\\:text-justify': {
        'text-align': 'justify',
      },
      '.text-left': {
        'text-align': 'left',
      },
      '.text-center': {
        'text-align': 'center',
      },
      '.text-right': {
        'text-align': 'right',
      },
      '.text-justify': {
        'text-align': 'justify',
      },
      '.normal-case': {
        'text-transform': 'none',
      },
      '.text-white': {
        '--text-opacity': 1,
        // color: '#FFF',
        color: 'rgba(255, 255, 255, var(--text-opacity))',
      },
      '.row-layout': {
        display: 'flex',
        'flex-direction': 'row',
      },
      '.column-layout': {
        display: 'flex',
        'flex-direction': 'column',
      },
      '.text-green': {
        color: '#4CAF50',
      },
      '.text-red': {
        color: '#F44336',
      },
      '.bpw-form-item-required::before': {
        display: 'inline-block',
        'margin-right': '4px',
        color: '#f5222d',
        'font-size': '14px',
        'font-family': 'SimSun, sans-serif',
        'line-height': '1',
        content: '*',
      },
    },
  })
);

const CustomeStyles = () => {
  useStyles();

  return null;
};

export default CustomeStyles;
