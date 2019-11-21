import {Component, OnInit, ViewChild} from '@angular/core';
import {formatDate } from '@angular/common';
import {MatPaginator} from '@angular/material/paginator';
import { SelectionModel } from '@angular/cdk/collections';
import { Router } from '@angular/router';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { WcmConfigService } from 'bpw-wcm-service';
import { WcmConfigurableComponent } from '../../components/wcm-configurable.component';
export interface Query {
  queryName: string;
  variable: string;
  description: string;
  numberOfEntries: number;
  lastEditDate: string;
}

@Component({
  selector: 'query-list',
  templateUrl: './query-list.component.html',
  styleUrls: ['./query-list.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ]
})
export class QueryListComponent extends WcmConfigurableComponent implements OnInit {
  columnProperties: string[] = ['select', 'queryName', 'variable', 'description', 'numberOfEntries', 'lastEditDate', 'actions'];
  displayedColumns: string[] = ['select', 'Query Name', 'Variable', 'Description', 'Entries', 'Last Edit Date', ' '];
  dataSource: MatTableDataSource<Query>;
  selection = new SelectionModel<Query>(true, []);

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private wcmConfigService: WcmConfigService,
      private router: Router) { 
    super(wcmConfigService);
    // Create 100 artifacts
    const artifacts = Array.from({length: 56}, (_, k) => createNewArtifact(k + 1));

    // Assign the data to the data source for the table to render
    this.dataSource = new MatTableDataSource(artifacts);
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const startPosition = this.paginator.pageSize * this.paginator.pageIndex;
    const allOtherItems = this.paginator.length - this.paginator.pageSize * this.paginator.pageIndex;
    const endPosition = startPosition + ((allOtherItems > this.paginator.pageSize) ? this.paginator.pageSize : allOtherItems);
    let allSelected = true;
    for (let index = startPosition; index < endPosition; index++) {
      if (!this.selection.isSelected(this.dataSource.data[index])) {
        allSelected = false;
        break;
      }
    }
    return allSelected;
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    const startPosition = this.paginator.pageSize * this.paginator.pageIndex;
    const allOtherItems = this.paginator.length - this.paginator.pageSize * this.paginator.pageIndex;
    const endPosition = startPosition + ((allOtherItems > this.paginator.pageSize) ? this.paginator.pageSize : allOtherItems);
    
    let allSelected = this.isAllSelected();
    for (let index = startPosition; index < endPosition; index++) {
      if (allSelected) {
        this.selection.deselect(this.dataSource.data[index]);
      } else {
        this.selection.select(this.dataSource.data[index]);
      }
    }
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: Query, rowIndex?: number): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${rowIndex + 1}`;
  }

  newQuery() {

  }

  editQuery(index: number) {
    const queryName = this.dataSource.data[index].queryName;
    this.router.navigate(
        ['/apps', 'query-builder', 'edit'], 
        {queryParams: {'queryName': queryName}});
  }

  deleteQuery(index: number) {

  }

  removeSelected() {
    this.dataSource.data = this.dataSource.data.filter(row => !this.selection.isSelected(row));
  }
}

/** Constants used to fill up our data base. */
const VARIABLES: string[] = [
  'maroon', 'red', 'orange', 'yellow', 'olive', 'green', 'purple', 'fuchsia', 'lime', 'teal',
  'aqua', 'blue', 'navy', 'black', 'gray'
];
const QUERY_NAMES: string[] = [
  'Maia', 'Asher', 'Olivia', 'Atticus', 'Amelia', 'Jack', 'Charlotte', 'Theodore', 'Isla', 'Oliver',
  'Isabella', 'Jasper', 'Cora', 'Levi', 'Violet', 'Arthur', 'Mia', 'Thomas', 'Elizabeth'
];
/** Builds and returns a new Artifact. */
function createNewArtifact(id: number): Query {
  const queryName = QUERY_NAMES[Math.round(Math.random() * (QUERY_NAMES.length - 1))] + ' ' +
      QUERY_NAMES[Math.round(Math.random() * (QUERY_NAMES.length - 1))].charAt(0) + '.';

  return {
    queryName: queryName,
    variable: VARIABLES[Math.round(Math.random() * (VARIABLES.length - 1))],
    numberOfEntries: Math.round(Math.random() * 20),
    lastEditDate: formatDate(new Date(2019, 0, 1), 'shortDate', 'en-US'),
    description: `Hydrogen is a chemical ...`
  };
}