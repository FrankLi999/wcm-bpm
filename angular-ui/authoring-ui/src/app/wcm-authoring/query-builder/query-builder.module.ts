import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
import { NgxDnDModule } from '@swimlane/ngx-dnd';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatRippleModule } from '@angular/material/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSortModule } from '@angular/material/sort';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTreeModule } from '@angular/material/tree';
import { MatNativeDateModule } from '@angular/material';
import { MatRadioModule } from '@angular/material/radio';

import { TranslateModule } from '@ngx-translate/core';

import { FuseSharedModule, FuseSidebarModule } from 'bpw-components';
import { QueryListComponent } from './query-list/query-list.component';
import { QueryEditorComponent } from './query-editor/query-editor.component';
import { SearchCriteriaComponent } from './search-criteria/search-criteria.component';

import {  
  QueryBuilderComponent,
  QueryInputDirective,
  QueryFieldDirective,
  QueryEntityDirective,
  QueryOperatorDirective,
  QueryButtonGroupDirective,
  QuerySwitchGroupDirective,
  QueryRemoveButtonDirective,
  QueryEmptyWarningDirective,
  QueryArrowIconDirective
} from './query-builder';

const routes: Routes = [
  {
      path       : 'query-builder/edit',
      component  : QueryEditorComponent
  },
  {
      path       : 'query-builder/list',
      component  : QueryListComponent
  }
];
@NgModule({
  declarations: [
    QueryListComponent,
    QueryEditorComponent,
    SearchCriteriaComponent,

    QueryBuilderComponent,
    QueryInputDirective,
    QueryOperatorDirective,
    QueryFieldDirective,
    QueryEntityDirective,
    QueryButtonGroupDirective,
    QuerySwitchGroupDirective,
    QueryRemoveButtonDirective,
    QueryEmptyWarningDirective,
    QueryArrowIconDirective
  ],
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    FormsModule, 
    ReactiveFormsModule,
    FlexLayoutModule,
    ScrollingModule,
    CdkTableModule,
    CdkTreeModule,
    NgxDnDModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatPaginatorModule,
    MatRippleModule,
    MatSelectModule,
    MatSortModule,
    MatTabsModule,
    MatTableModule,
    MatToolbarModule,
    MatTreeModule,
    MatNativeDateModule,
    MatRadioModule,

    TranslateModule,

    FuseSharedModule,
    FuseSidebarModule
  ]
})
export class QueryBuilderModule { }
