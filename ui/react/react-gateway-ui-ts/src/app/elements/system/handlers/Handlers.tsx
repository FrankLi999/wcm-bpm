import React from 'react';
import { useDispatch } from 'react-redux';
import { makeStyles } from '@material-ui/core/styles';
import Icon from '@material-ui/core/Icon';
import Button from '@material-ui/core/Button';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import TableFooter from '@material-ui/core/TableFooter';
import TablePagination from '@material-ui/core/TablePagination';
import KeyboardArrowDownIcon from '@material-ui/icons/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@material-ui/icons/KeyboardArrowUp';
import Box from '@material-ui/core/Box';
import Collapse from '@material-ui/core/Collapse';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import TablePaginationActions from 'bpw-common/elements/TablePaginationActions';
import { openNewSystemDialog, openEditSystemDialog } from '../../../store/system/systemDialogSlice';
import RequestHandlerDialog from './RequestHandlerDialog';

const useStyles = makeStyles({
  table: {
    minWidth: 400,
  },
});

function createData(name, active, updated, extraData) {
  return { name, active, updated, extraData };
}

const rows = [
  createData('wcm-Rewrite', true, '2020-09-19 22:38:11', 'extraData'),
  createData('wcm-Rewrite1', true, '2020-09-19 22:38:11', 'extraData1'),
  createData('bpm-Rewrite2', true, '2020-09-19 22:38:11', 'extraData2'),
  createData('bpm-Rewrite3', true, '2020-09-19 22:38:11', 'extraData3'),
  createData('bpm-Rewrite4', true, '2020-09-19 22:38:11', 'extraData4'),
];

function RequestHandlersRow(props) {
  const { row, rowIndex, onEdit, onRemoveRow } = props;
  const [open, setOpen] = React.useState(false);
  return (
    <>
      <TableRow key={row.name}>
        <TableCell component="th" scope="row">
          <IconButton aria-label="expand row" size="small" onClick={() => setOpen(!open)}>
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        <TableCell component="th" scope="row">
          {row.name}
        </TableCell>
        <TableCell align="center">
          {row.active ? <Icon className="text-green text-20">check_circle</Icon> : <Icon className="text-red text-20">remove_circle</Icon>}
        </TableCell>
        <TableCell align="center">{row.updated}</TableCell>
        <TableCell align="center">
          <div className="px-8">
            <IconButton
              onClick={(ev) => {
                ev.preventDefault();
                ev.stopPropagation();
                onEdit(row, rowIndex);
              }}
            >
              <Icon>edit</Icon>
            </IconButton>
            <IconButton
              onClick={(ev) => {
                ev.preventDefault();
                ev.stopPropagation();
                onRemoveRow(rowIndex);
              }}
            >
              <Icon>delete</Icon>
            </IconButton>
          </div>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box margin={1}>
              <Typography variant="h6" gutterBottom component="div">
                {row.extraData}
              </Typography>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
}
function RequestHandlers(props) {
  const classes = useStyles();
  const dispatch = useDispatch();
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(3);

  const handleNewDialog = () => {
    dispatch(openNewSystemDialog({ dialogType: 'handler', data: {} }));
  };

  const handleEditDialog = (row, rowIndex) => {
    dispatch(openEditSystemDialog({ dialogType: 'handler', rowIndex: rowIndex, data: { title: rowIndex, notes: rowIndex } }));
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const removeRow = (rowIndex) => {
    alert(`remove ${rowIndex}`);
  };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', margin: '2.4rem' }}>
      <div style={{ display: 'flex', flexDirection: 'row', paddingBottom: '12px' }}>
        <h3 style={{ width: '40%' }}>Selector rule table</h3>
        <Button style={{ width: '30%' }} className="whitespace-no-wrap normal-case" variant="contained" color="secondary">
          <span>Synchronize</span>
        </Button>
        <Button
          onClick={() => {
            handleNewDialog('new');
          }}
          style={{ width: '30%' }}
          className="whitespace-no-wrap normal-case"
          variant="contained"
          color="secondary"
        >
          <span>New</span>
        </Button>
      </div>
      <div style={{ height: 400, width: '100%' }}>
        <TableContainer component={Paper}>
          <Table className={classes.table} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell component="th" scope="row">
                  &nbsp;
                </TableCell>
                <TableCell>Rule Name</TableCell>
                <TableCell align="center">Active</TableCell>
                <TableCell align="center">Updated</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {(rowsPerPage > 0 ? rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage) : rows).map((row, rowIndex) => (
                <RequestHandlersRow key={row.name} row={row} rowIndex={rowIndex} onEdit={handleEditDialog} onRemoveRow={removeRow} />
              ))}
            </TableBody>
          </Table>
          <TableFooter>
            <TableRow>
              <TablePagination
                rowsPerPageOptions={[3, 5, 10, { label: 'All', value: -1 }]}
                colSpan={3}
                count={rows.length}
                rowsPerPage={rowsPerPage}
                page={page}
                SelectProps={{
                  inputProps: { 'aria-label': 'rows per page' },
                  native: true,
                }}
                onChangePage={handleChangePage}
                onChangeRowsPerPage={handleChangeRowsPerPage}
                ActionsComponent={TablePaginationActions}
              />
            </TableRow>
          </TableFooter>
        </TableContainer>
      </div>
      <RequestHandlerDialog />
    </div>
  );
}

export default RequestHandlers;
