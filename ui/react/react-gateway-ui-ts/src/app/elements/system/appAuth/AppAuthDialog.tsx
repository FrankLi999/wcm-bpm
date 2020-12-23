import React, { useCallback, useEffect, useState } from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import FormControl from '@material-ui/core/FormControl';
import Icon from '@material-ui/core/Icon';
import IconButton from '@material-ui/core/IconButton';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import TextField from '@material-ui/core/TextField';
import FormLabel from '@material-ui/core/FormLabel';
import FormGroup from '@material-ui/core/FormGroup';
import Switch from '@material-ui/core/Switch';
import Typography from '@material-ui/core/Typography';
import { useDispatch, useSelector } from 'react-redux';
import { Formik, ErrorMessage, FieldArray } from 'formik';
import * as Yup from 'yup';
import _ from 'bpw-common/lodash';
// import { useForm } from 'bpw-common/hooks';
// import BpwUtils from 'bpw-common/utils';

import { closeNewSystemDialog, closeEditSystemDialog } from '../../../store/system/systemDialogSlice';

function AppAuthDialog(props) {
  const { dialogType, data } = props;
  const dispatch = useDispatch();
  const systemDialog = useSelector(({ system }) => system.dialog.systemDialog);
  // const { form, handleChange, setForm } = useForm({ ...defaultFormState });
  const [open, setOpen] = React.useState(false);
  // const initDialog = useCallback(() => {
  //   if (selectorDialog.type === 'edit' && selectorDialog.data) {
  //     /**
  //      * Dialog type: 'edit'
  //      */
  //     setForm({ ...selectorDialog.data });
  //   }

  //   /**
  //    * Dialog type: 'new'
  //    */
  //   if (selectorDialog.type === 'new') {
  //     setForm({
  //       ...defaultFormState,
  //       ...selectorDialog.data,
  //       id: BpwUtils.generateGUID(),
  //     });
  //   }
  // }, [selectorDialog.data, selectorDialog.selectorType, selectorDialog.type, setForm]);

  useEffect(() => {
    /**
     * After Dialog Open
     */
    if (systemDialog.props.open && systemDialog.dialogType === 'appAuth') {
      // initDialog();
      setOpen(true);
    } else {
      setOpen(false);
    }
  }, [systemDialog.props.open]);

  function closeSystemDialog() {
    return systemDialog.type === 'edit'
      ? dispatch(closeEditSystemDialog({ dialogType: 'appAuth' }))
      : dispatch(closeNewSystemDialog({ dialogType: 'appAuth' }));
  }

  function canBeSubmitted() {
    //   return form.name.length > 0;
    return false;
  }

  function handleSubmit() {
    //todo
  }

  function handleConditionChange(arrayHelpers, index) {
    // arrayHelpers.replace(index, {
    //   param: 'xxxx',
    //   value: 'yyyy',
    // });
    arrayHelpers.replace(index, {
      param: 'xxxx',
      value: 'yyyy',
    });
  }
  return (
    <Dialog
      open={open}
      fullWidth
      maxWidth="sm"
      classes={{
        paper: 'rounded-8',
      }}
      aria-labelledby="form-dialog-title"
    >
      <DialogTitle id="form-dialog-title" elevation={1}>
        <Typography variant="subtitle1" color="inherit">
          {systemDialog.type === 'new' ? 'New Authentication' : 'Edit Authentication'}
        </Typography>
      </DialogTitle>
      <Formik
        initialValues={{
          ...systemDialog.data,
        }}
        onSubmit={handleSubmit}
        validationSchema={Yup.object().shape({
          name: Yup.string()
            //.email()
            .required('Enter valid name')
            .min(6, 'Min character length is 6'),
          serviceName: Yup.string().required('Please valid service name').min(4, 'Min character length is 4'),
        })}
      >
        {(props) => {
          const { values, touched, errors, isValid, isSubmitting, handleChange, handleBlur, handleSubmit } = props;
          return (
            <form className="flex flex-col justify-center w-full" onSubmit={handleSubmit}>
              <DialogContent classes={{ root: 'p-0' }}>
                <div className="px-16 sm:px-24">
                  <FormControl className="mt-8 mb-16" required fullWidth>
                    <TextField label="Name" autoFocus name="name" value={values.name} onChange={handleChange} required variant="outlined" />
                  </FormControl>
                  <FormControl className="mt-8 mb-16" variant="outlined" fullWidth>
                    <InputLabel htmlFor="outlined-age-native-simple">Conditions</InputLabel>
                    <Select
                      native
                      value={values.andOr}
                      onChange={handleChange}
                      label="Conditions"
                      inputProps={{
                        name: 'andOr',
                        id: 'outlined-age-native-simple',
                      }}
                    >
                      <option value={'and'}>And</option>
                      <option value={'or'}>Or</option>
                    </Select>
                  </FormControl>
                  <FieldArray
                    name="conditions"
                    value="values.conditions"
                    render={(arrayHelpers) => (
                      <div>
                        <h3 style={{ width: '90px', 'font-size': '14px', 'margin-top': '.8rem' }}>Conditions:</h3>
                        {values.conditions && values.conditions.length > 0 ? (
                          values.conditions.map((condition, index) => (
                            <FormGroup key={index} className="mt-8 mb-16" row>
                              <FormControl className="mt-8 mb-16" variant="outlined">
                                <Select native name={`conditions[${index}].param`} value={values.conditions[index].param} onChange={handleChange}>
                                  <option value={'post'}>post</option>
                                  <option value={'uri'}>uri</option>
                                  <option value={'query'}>query</option>
                                  <option value={'host'}>host</option>
                                  <option value={'ip'}>ip</option>
                                  <option value={'header'}>header</option>
                                </Select>
                              </FormControl>
                              <FormControl className="mt-8 mb-16" variant="outlined">
                                <Select native name={`conditions[${index}].equation`} value={values.conditions[index].equation} onChange={handleChange}>
                                  <option value={'match'}>match</option>
                                  <option value={'='}>=</option>
                                  <option value={'regEx'}>regEx</option>
                                  <option value={'like'}>like</option>
                                </Select>
                              </FormControl>
                              <FormControl style={{ width: '25%' }} className="mt-8 mb-16" required>
                                <TextField
                                  name={`conditions[${index}].value`}
                                  value={values.conditions[index].value}
                                  onChange={handleChange}
                                  variant="outlined"
                                />
                              </FormControl>
                              <IconButton
                                onClick={() => arrayHelpers.remove(index)} // remove a friend from the list
                              >
                                <Icon>remove_circle</Icon>
                              </IconButton>
                              <IconButton
                                onClick={() => arrayHelpers.insert(index, { param: 'uri', equation: '=', value: '' })} // insert an empty string at a position
                              >
                                <Icon>add_circle</Icon>
                              </IconButton>
                            </FormGroup>
                          ))
                        ) : (
                          <button type="button" onClick={() => arrayHelpers.push({ param: 'uri', equation: '=', value: '' })}>
                            Add a condition
                          </button>
                        )}
                      </div>
                    )}
                  />
                  <FormGroup className="mt-8 mb-16" row>
                    <FormLabel className="mt-8 mb-16" component="legend">
                      <h3 style={{ width: '90px', 'font-size': '14px', 'margin-top': '.8rem' }}>springCloud:</h3>
                    </FormLabel>

                    <FormControl className="mt-8 mb-16  mr-8" required>
                      <TextField label="Path" name="springCloudPath" value={values.springCloudPath} onChange={handleChange} variant="outlined" />
                    </FormControl>
                    <FormControl className="mt-8 mb-16" required>
                      <TextField
                        required
                        label="Timeout (ms)"
                        name="springCloudTimeout"
                        value={values.springCloudTimeout}
                        onChange={handleChange}
                        variant="outlined"
                      />
                    </FormControl>
                  </FormGroup>
                  <FormGroup className="mt-8 mb-16" row>
                    <FormControl className="mt-8 mb-16" style={{ display: 'flex', flexDirection: 'row' }}>
                      <FormLabel required>Logging</FormLabel>
                      <Switch
                        checked={values.logging}
                        onChange={handleChange}
                        color="primary"
                        name="logging"
                        inputProps={{ 'aria-label': 'secondary checkbox' }}
                      />
                    </FormControl>
                    <FormControl className="mt-8 mb-16" style={{ display: 'flex', flexDirection: 'row' }}>
                      <FormLabel required>Active</FormLabel>
                      <Switch checked={values.active} onChange={handleChange} name="active" inputProps={{ 'aria-label': 'primary checkbox' }} />
                    </FormControl>
                  </FormGroup>

                  <FormControl className="mt-8 mb-16" required fullWidth>
                    <TextField label="Order" name="order" value={values.order} onChange={handleChange} variant="outlined" />
                  </FormControl>
                </div>
              </DialogContent>

              <DialogActions className="justify-between p-8">
                {systemDialog.type === 'new' ? (
                  <div className="px-16">
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => {
                        // dispatch(addSelector(form)).then(() => {
                        closeSystemDialog();
                        // });
                      }}
                      disabled={!canBeSubmitted()}
                    >
                      Add
                    </Button>
                  </div>
                ) : (
                  <div className="px-16">
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => {
                        // dispatch(updateSelector(form)).then(() => {
                        closeSystemDialog();
                        // });
                      }}
                      disabled={!canBeSubmitted()}
                    >
                      Save
                    </Button>
                  </div>
                )}
                <IconButton
                  className="min-w-auto"
                  onClick={() => {
                    // dispatch(removeTodo(form.id)).then(() => {
                    closeSystemDialog();
                    // });
                  }}
                >
                  <Icon>cancel</Icon>
                </IconButton>
              </DialogActions>
            </form>
          );
        }}
      </Formik>
    </Dialog>
  );
}

export default AppAuthDialog;
