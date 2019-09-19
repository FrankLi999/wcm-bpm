import {Component, OnInit, ViewChild} from '@angular/core';
import {formatDate } from '@angular/common';
import {MatPaginator} from '@angular/material/paginator';
import { Router } from '@angular/router';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {animate, state, style, transition, trigger} from '@angular/animations';

export interface ResourceTypeData {
  baseResourceType: string;
  resourceTypeName: string;
  variable: string;
  description: string;
  numberOfEntries: number;
  lastEditDate: string;
}

/** Constants used to fill up our data base. */
const VARIABLES: string[] = [
  'maroon', 'red', 'orange', 'yellow', 'olive', 'green', 'purple', 'fuchsia', 'lime', 'teal',
  'aqua', 'blue', 'navy', 'black', 'gray'
];
const RESOURCE_TYPE_NAMES: string[] = [
  'Maia', 'Asher', 'Olivia', 'Atticus', 'Amelia', 'Jack', 'Charlotte', 'Theodore', 'Isla', 'Oliver',
  'Isabella', 'Jasper', 'Cora', 'Levi', 'Violet', 'Arthur', 'Mia', 'Thomas', 'Elizabeth'
];

const BASE_RESOURCE_TYPE: string[] = [
  'Content', 'Page', 'Widget', 'File', 'Key/Value', 'VanityURL', 'Form', 'Persona'
];

@Component({
  selector: 'resource-type-list',
  templateUrl: './resource-type-list.component.html',
  styleUrls: ['./resource-type-list.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ]
})
export class ResourceTypeListComponent implements OnInit {
  columnProperties: string[] = ['baseResourceType', 'resourceTypeName', 'variable', 'description', 'numberOfEntries', 'lastEditDate', 'actions'];
  displayedColumns: string[] = [' ', 'Resource Type Name', 'Variable', 'Description', 'Entries', 'Last Edit Date', ' '];
  dataSource: MatTableDataSource<ResourceTypeData>;
  // expandedElement: ResourceTypeData | null;

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(
      private router: Router
  ) {
    // Create 100 artifacts
    const artifacts = Array.from({length: 100}, (_, k) => createNewArtifact(k + 1));

    // Assign the data to the data source for the table to render
    this.dataSource = new MatTableDataSource(artifacts);
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  getIcon(baseResourceType: string) {
    let resourceTypeIndex = BASE_RESOURCE_TYPE.indexOf(baseResourceType);
    let resourceTypeIcon = '';
    switch (resourceTypeIndex) { //more_vert
      case 0:
          resourceTypeIcon = 'archive';
          break;
      case 1:
          resourceTypeIcon = 'pages';
          break;
      case 2:
          resourceTypeIcon = 'build';
          break;
      case 3:
          resourceTypeIcon = 'file_copy';
          break;
      case 4:
          resourceTypeIcon = 'domain';
          break;
      case 5:
          resourceTypeIcon = 'http';
          break;                                    
      case 6:
          resourceTypeIcon = 'apps';
          break;
      case 7:
      default:  
          resourceTypeIcon = 'person_outline';
          break;
    }
    return resourceTypeIcon;
  }

  editResourceType(item: number) {
      const resourceTypeName = 'aaa';
      this.router.navigate(
          ['/wcm-authoring', 'resource-type', 'edit'], 
          {queryParams: {'resourceTypeName': resourceTypeName}});
  }

  deleteResourceType(item: number) {
    alert(`Click on Action delete for ${item}`);
  }

  newContentType() {
    return false;
  }

  newFileType() {
    return false;
  }

  newWidgetType() {
    return false;
  }

  newKeyValueType() {
    return false;
  }

  newVanityURLType() {
    return false;
  }

  newPageType() {
    return false;
  }
  newPersonaType() {
    return false;
  }
}

/** Builds and returns a new Artifact. */
function createNewArtifact(id: number): ResourceTypeData {
  const resourceTypeName = RESOURCE_TYPE_NAMES[Math.round(Math.random() * (RESOURCE_TYPE_NAMES.length - 1))] + ' ' +
      RESOURCE_TYPE_NAMES[Math.round(Math.random() * (RESOURCE_TYPE_NAMES.length - 1))].charAt(0) + '.';

  return {
    baseResourceType: BASE_RESOURCE_TYPE[Math.round(Math.random() * (BASE_RESOURCE_TYPE.length - 1))],
    resourceTypeName: resourceTypeName,
    variable: VARIABLES[Math.round(Math.random() * (VARIABLES.length - 1))],
    numberOfEntries: Math.round(Math.random() * 20),
    lastEditDate: formatDate(new Date(2019, 0, 1), 'shortDate', 'en-US'),
    description: `Hydrogen is a chemical ...`
  };
}