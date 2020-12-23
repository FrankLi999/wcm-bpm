import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormLabel from '@material-ui/core/FormLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import Select from '@material-ui/core/Select';
import { makeStyles } from '@material-ui/core/styles';
import Switch from '@material-ui/core/Switch';
import Typography from '@material-ui/core/Typography';
import clsx from 'clsx';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { updateUserSettings } from 'bpw-auth/store/userSlice';
import { setDefaultSettings } from '../../store/settingsSlice';
import BpwLayoutConfigs from '../../layout/BpwLayoutConfigs';
import _ from '../../lodash';
const useStyles = makeStyles((theme) => ({
  root: {},
  formControl: {
    margin: '6px 0',
    width: '100%',
    '&:last-child': {
      marginBottom: 0,
    },
  },
  group: {},
  formGroupTitle: {
    position: 'absolute',
    top: -10,
    left: 8,
    fontWeight: 600,
    padding: '0 4px',
    backgroundColor: theme.palette.background.paper,
  },
  formGroup: {
    position: 'relative',
    border: `1px solid ${theme.palette.divider}`,
    borderRadius: 2,
    padding: '12px 12px 0 12px',
    margin: '24px 0 16px 0',
    '&:first-of-type': {
      marginTop: 16,
    },
  },
}));

function BpwSettings(props) {
  const dispatch = useDispatch();
  const user = useSelector(({ auth }) => auth.user);
  const themes = useSelector(({ bpw }) => bpw.settings.themes);
  const settings = useSelector(({ bpw }) => bpw.settings.current);

  const classes = useStyles(props);

  function handleChange(event) {
    const newSettings = _.set(_.merge({}, settings), event.target.name, event.target.type === 'checkbox' ? event.target.checked : event.target.value);

    /**
     * If layout style changes,
     * Reset Layout Configuration
     */
    if (event.target.name === 'layout.title' && event.target.value !== settings.layout.title) {
      newSettings.layout = {
        "title": event.target.value
      }
    }

    if (user.role === 'guest') {
      dispatch(setDefaultSettings(newSettings));
    } else {
      dispatch(updateUserSettings(newSettings));
    }
  }

  const ThemeSelect = ({ value, name, handleThemeChange }) => {
    return (
      <Select
        className="w-full rounded-8 h-40 overflow-hidden my-8"
        value={value}
        onChange={handleThemeChange}
        name={name}
        variant="outlined"
        style={{
          backgroundColor: themes[value].palette.background.default,
          color: themes[value].palette.type === 'light' ? '#000000' : '#ffffff',
        }}
      >
        {Object.entries(themes)
          .filter(([key, val]) => !(name === 'theme.main' && (key === 'mainThemeDark' || key === 'mainThemeLight')))
          .map(([key, val]) => (
            <MenuItem
              key={key}
              value={key}
              className="m-8 mt-0 rounded-lg"
              style={{
                backgroundColor: val.palette.background.default,
                color: val.palette.type === 'light' ? '#000000' : '#ffffff',
                border: `1px solid ${val.palette.type === 'light' ? 'rgba(0, 0, 0, 0.12)' : 'rgba(255, 255, 255, 0.12)'}`,
              }}
            >
              {_.startCase(key)}
              <div
                className="flex w-full h-8 block absolute bottom-0 left-0 right-0"
                style={{
                  borderTop: `1px solid ${val.palette.type === 'light' ? 'rgba(0, 0, 0, 0.12)' : 'rgba(255, 255, 255, 0.12)'}`,
                }}
              >
                <div
                  className="w-1/4 h-8"
                  style={{
                    backgroundColor: val.palette.primary.main ? val.palette.primary.main : val.palette.primary[500],
                  }}
                />
                <div
                  className="w-1/4 h-8"
                  style={{
                    backgroundColor: val.palette.secondary.main ? val.palette.secondary.main : val.palette.secondary[500],
                  }}
                />
                <div
                  className="w-1/4 h-8"
                  style={{
                    backgroundColor: val.palette.error.main ? val.palette.error.main : val.palette.error[500],
                  }}
                />
                <div className="w-1/4 h-8" style={{ backgroundColor: val.palette.background.paper }} />
              </div>
            </MenuItem>
          ))}
      </Select>
    );
  };

  const LayoutSelect = () => (
    <FormControl component="fieldset" className={classes.formControl}>
      <FormLabel component="legend" className="text-10">
        Style
      </FormLabel>

      <RadioGroup aria-label="Layout Title" name="layout.title" className={classes.group} value={settings.layout.title} onChange={handleChange}>
        {Object.entries(BpwLayoutConfigs).map(([key, layout]) => (
          <FormControlLabel key={key} value={key} control={<Radio />} label={layout.title} />
        ))}
      </RadioGroup>
    </FormControl>
  );

  const DirectionSelect = () => (
    <FormControl component="fieldset" className={classes.formControl}>
      <FormLabel component="legend" className="">
        Direction
      </FormLabel>

      <RadioGroup aria-label="Layout Style" name="direction" className={classes.group} value={settings.direction} onChange={handleChange} row>
        <FormControlLabel key="rtl" value="rtl" control={<Radio />} label="RTL" />
        <FormControlLabel key="ltr" value="ltr" control={<Radio />} label="LTR" />
      </RadioGroup>
    </FormControl>
  );

  const getForm = (form, prefix) =>
    Object.entries(form).map(([key, formControl]) => {
      const target = prefix ? `${prefix}.${key}` : key;
      switch (formControl.type) {
        case 'radio': {
          return (
            <FormControl key={target} component="fieldset" className={classes.formControl}>
              <FormLabel component="legend" className="text-10">
                {formControl.title}
              </FormLabel>
              <RadioGroup
                aria-label={formControl.title}
                name={`layout.${target}`}
                className={classes.group}
                value={_.get(settings.layout, target)}
                onChange={handleChange}
                row={formControl.options.length < 4}
              >
                {formControl.options.map((opt) => (
                  <FormControlLabel key={opt.value} value={opt.value} control={<Radio />} label={opt.name} />
                ))}
              </RadioGroup>
            </FormControl>
          );
        }
        case 'switch': {
          return (
            <FormControl key={target} component="fieldset" className={classes.formControl}>
              <FormControlLabel
                classes={
                  {
                    // root: "flex-row-reverse justify-end pl-16"
                  }
                }
                control={
                  <Switch
                    name={`layout.${target}`}
                    checked={_.get(settings.layout, target)}
                    onChange={handleChange}
                    aria-label={formControl.title}
                  />
                }
                label={
                  <FormLabel component="legend" className="text-10">
                    {formControl.title}
                  </FormLabel>
                }
              />
            </FormControl>
          );
        }
        case 'group': {
          return (
            <div key={target} className={classes.formGroup}>
              <Typography className={classes.formGroupTitle} color="textSecondary">
                {formControl.title}
              </Typography>

              {getForm(formControl.children, key)}
            </div>
          );
        }
        default: {
          return '';
        }
      }
    });

  function LayoutConfig() {
    const { form } = BpwLayoutConfigs[settings.layout.title];
    return getForm(form);
  }

  return (
    <div className={classes.root}>
      <div className={classes.formGroup}>
        <Typography className={classes.formGroupTitle} color="textSecondary">
          Layout
        </Typography>

        <LayoutSelect />

        <LayoutConfig />

        <Typography className="my-16 text-12 italic" color="textSecondary">
          *Not all option combinations are available
        </Typography>
      </div>

      <div className={clsx(classes.formGroup, 'pb-16')}>
        <Typography className={classes.formGroupTitle} color="textSecondary">
          Theme
        </Typography>

        <FormControl component="fieldset" className={classes.formControl}>
          <FormLabel component="legend" className="text-10">
            Main
          </FormLabel>
          <ThemeSelect value={settings.theme.main} name="theme.main" handleThemeChange={handleChange} />
        </FormControl>
        <FormControl component="fieldset" className={classes.formControl}>
          <FormLabel component="legend" className="04">
            Navbar
          </FormLabel>
          <ThemeSelect value={settings.theme.navbar} name="theme.navbar" handleThemeChange={handleChange} />
        </FormControl>
        <FormControl component="fieldset" className={classes.formControl}>
          <FormLabel component="legend" className="text-10">
            Toolbar
          </FormLabel>
          <ThemeSelect value={settings.theme.toolbar} name="theme.toolbar" handleThemeChange={handleChange} />
        </FormControl>
        <FormControl component="fieldset" className={classes.formControl}>
          <FormLabel component="legend" className="text-10">
            Footer
          </FormLabel>
          <ThemeSelect value={settings.theme.footer} name="theme.footer" handleThemeChange={handleChange} />
        </FormControl>
      </div>

      <FormControl component="fieldset" className={classes.formControl}>
        <FormLabel component="legend" className="text-10">
          Custom Scrollbars
        </FormLabel>
        <Switch checked={settings.customScrollbars} onChange={handleChange} aria-label="Custom Scrollbars" name="customScrollbars" />
      </FormControl>

      <FormControl component="fieldset" className={classes.formControl}>
        <FormLabel component="legend" className="text-10">
          Animations
        </FormLabel>
        <Switch checked={settings.animations} onChange={handleChange} aria-label="Animations" name="animations" />
      </FormControl>

      <DirectionSelect />
    </div>
  );
}

export default React.memo(BpwSettings);
