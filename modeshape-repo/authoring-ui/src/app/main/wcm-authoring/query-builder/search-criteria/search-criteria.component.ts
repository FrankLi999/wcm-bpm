import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { QueryBuilderClassNames, QueryBuilderConfig, RuleSet } from '../query-builder';

export interface QueryModel {
  queryName: string;
  variable: string;
  description: string;
  numberOfEntries: number;
  // resourceType: string;
  siteArea: string;
  nestedLevel: number;
  category: string;
  publishDateAfter: Date;
  publishDateBefore: Date;
  query: RuleSet;
}

@Component({
  selector: 'search-criteria',
  templateUrl: './search-criteria.component.html',
  styleUrls: ['./search-criteria.component.scss']
})
export class SearchCriteriaComponent implements OnInit {
  public bootstrapClassNames: QueryBuilderClassNames = {
    removeIcon: 'fa fa-minus',
    addIcon: 'fa fa-plus',
    arrowIcon: 'fa fa-chevron-right px-2',
    button: 'btn',
    buttonGroup: 'btn-group',
    rightAlign: 'order-12 ml-auto',
    switchRow: 'd-flex px-2',
    switchGroup: 'd-flex align-items-center',
    switchRadio: 'custom-control-input',
    switchLabel: 'custom-control-label',
    switchControl: 'custom-control custom-radio custom-control-inline',
    row: 'row p-2 m-1',
    rule: 'border',
    ruleSet: 'border',
    invalidRuleSet: 'alert alert-danger',
    emptyWarning: 'text-danger mx-auto',
    operatorControl: 'form-control',
    operatorControlSize: 'col-auto pr-0',
    fieldControl: 'form-control',
    fieldControlSize: 'col-auto pr-0',
    entityControl: 'form-control',
    entityControlSize: 'col-auto pr-0',
    inputControl: 'form-control',
    inputControlSize: 'col-auto'
  };

  public query = {
    condition: 'and',
    rules: [
      {field: 'age', operator: '<=', entity: 'physical'},
      {field: 'birthday', operator: '=', value: new Date(), entity: 'nonphysical'},
      {
        condition: 'or',
        rules: [
          {field: 'gender', operator: '=', entity: 'physical'},
          {field: 'occupation', operator: 'in', entity: 'nonphysical'},
          {field: 'school', operator: 'is null', entity: 'nonphysical'},
          {field: 'notes', operator: '=', entity: 'nonphysical'}
        ]
      }
    ]
  };

  public entityConfig: QueryBuilderConfig = {
    entities: {
      physical: {name: 'Physical Attributes'},
      nonphysical: {name: 'Nonphysical Attributes'}
    },
    fields: {
      age: {name: 'Age', type: 'number', entity: 'physical'},
      gender: {
        name: 'Gender',
        entity: 'physical',
        type: 'category',
        options: [
          {name: 'Male', value: 'm'},
          {name: 'Female', value: 'f'}
        ]
      },
      name: {name: 'Name', type: 'string', entity: 'nonphysical'},
      notes: {name: 'Notes', type: 'textarea', operators: ['=', '!='], entity: 'nonphysical'},
      educated: {name: 'College Degree?', type: 'boolean', entity: 'nonphysical'},
      birthday: {name: 'Birthday', type: 'date', operators: ['=', '<=', '>'],
        defaultValue: (() => new Date()), entity: 'nonphysical'
      },
      school: {name: 'School', type: 'string', nullable: true, entity: 'nonphysical'},
      occupation: {
        name: 'Occupation',
        entity: 'nonphysical',
        type: 'category',
        options: [
          {name: 'Student', value: 'student'},
          {name: 'Teacher', value: 'teacher'},
          {name: 'Unemployed', value: 'unemployed'},
          {name: 'Scientist', value: 'scientist'}
        ]
      }
    }
  };

  // public config: QueryBuilderConfig = {
  //   fields: {
  //     age: {name: 'Age', type: 'number'},
  //     gender: {
  //       name: 'Gender',
  //       type: 'category',
  //       options: [
  //         {name: 'Male', value: 'm'},
  //         {name: 'Female', value: 'f'}
  //       ]
  //     },
  //     name: {name: 'Name', type: 'string'},
  //     notes: {name: 'Notes', type: 'textarea', operators: ['=', '!=']},
  //     educated: {name: 'College Degree?', type: 'boolean'},
  //     birthday: {name: 'Birthday', type: 'date', operators: ['=', '<=', '>'],
  //       defaultValue: (() => new Date())
  //     },
  //     school: {name: 'School', type: 'string', nullable: true},
  //     occupation: {
  //       name: 'Occupation',
  //       type: 'category',
  //       options: [
  //         {name: 'Student', value: 'student'},
  //         {name: 'Teacher', value: 'teacher'},
  //         {name: 'Unemployed', value: 'unemployed'},
  //         {name: 'Scientist', value: 'scientist'}
  //       ]
  //     }
  //   }
  // };

  public currentConfig: QueryBuilderConfig;
  public allowRuleset: boolean = true;
  public allowCollapse: boolean = true;

  @Input() queryName: string;
  searchCriteriaForm: FormGroup;
  public queryCtrl: FormControl;
  queryModel: QueryModel = {
    queryName: 'qn_1',
    variable: 'qv_1',
    description: 'q_desc_1',
    numberOfEntries: 10,
    // resourceType: 'res_type_1',
    siteArea: 'sa_1',
    nestedLevel: 1,
    category: 'cat_1',
    publishDateAfter: new Date(),
    publishDateBefore: new Date(),
    query: this.query
  }
  constructor(private formBuilder: FormBuilder,) { }

  ngOnInit() {
    this.queryCtrl = this.formBuilder.control(this.query);
    this.currentConfig = this.entityConfig;
    this.searchCriteriaForm = this.createSearchCriteriaForm();
  }

  saveSearchCriteria() {
    console.log(this.searchCriteriaForm.value);
    return false;
  }

  resetSearchCriteria() {
    return false;
  }

  createSearchCriteriaForm(): FormGroup {
    return this.formBuilder.group({
      queryName         : [this.queryModel.queryName],
      variable          : [this.queryModel.variable],
      description       : [this.queryModel.description],
      numberOfEntries   : [this.queryModel.numberOfEntries],
      nestedLevel       : [this.queryModel.nestedLevel],
      //resourceType      : [this.queryModel.resourceType],
      siteArea          : [this.queryModel.siteArea],
      category          : [this.queryModel.category],
      publishDateAfter  : [this.queryModel.publishDateAfter],
      publishDateBefore : [this.queryModel.publishDateBefore],
      query             : [this.queryModel.query],
   });
  }
}
