import React from 'react';
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
import { useDispatch } from 'react-redux';
import IconButton from '@material-ui/core/IconButton';
import TablePaginationActions from 'bpw-common/elements/TablePaginationActions';
import SpringCloudSelectorDialog from './SelectorDialog';
import { openNewSelectorDialog, openEditSelectorDialog } from '../../../../store/handlers/handlerDialogSlice';
const useStyles = makeStyles({
  table: {
    minWidth: 400,
  },
});

function createData(name, active) {
  return { name, active };
}

const rows = [
  createData('wcm-SpringCloud', true),
  createData('wcm-SpringCloud1', true),
  createData('bpm-SpringCloud2', true),
  createData('bpm-SpringCloud3', true),
  createData('bpm-SpringCloud4', true),
];

function SpringCloudSelectorRow(props) {
  const { row, rowIndex, onEdit, onRemoveRow } = props;
  return (
    <TableRow key={row.name}>
      <TableCell component="th" scope="row">
        {row.name}
      </TableCell>
      <TableCell align="center">
        {row.active ? <Icon className="text-green text-20">check_circle</Icon> : <Icon className="text-red text-20">remove_circle</Icon>}
      </TableCell>
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
  );
}
function SpringCloudSelectors(props) {
  const classes = useStyles();
  const dispatch = useDispatch();
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(3);

  const handleNewDialog = () => {
    dispatch(openNewSelectorDialog({ selectorType: 'springCloud' }));
  };

  const handleEditDialog = (row, rowIndex) => {
    dispatch(openEditSelectorDialog({ selectorType: 'springCloud', rowIndex: rowIndex, data: { title: rowIndex, notes: rowIndex } }));
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
        <h3 style={{ width: '70%' }}>Selector table</h3>
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
                <TableCell>Name</TableCell>
                <TableCell align="center">Active</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {(rowsPerPage > 0 ? rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage) : rows).map((row, rowIndex) => (
                <SpringCloudSelectorRow key={row.name} row={row} rowIndex={rowIndex} onEdit={handleEditDialog} onRemoveRow={removeRow} />
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
      <SpringCloudSelectorDialog />
    </div>
  );
}

export default SpringCloudSelectors;
