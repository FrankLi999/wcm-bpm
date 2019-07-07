import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
export interface QueryModel {
  queryName: string;
  variable: string;
  description: string;
  numberOfEntries: number;
  resourceType: string;
  siteArea: string;
  nestedLevel: number;
  category: string;
  publishDateAfter: Date;
  publishDateBefore: Date;
}

@Component({
  selector: 'search-criteria',
  templateUrl: './search-criteria.component.html',
  styleUrls: ['./search-criteria.component.scss']
})
export class SearchCriteriaComponent implements OnInit {
  @Input() queryName: string;
  searchCriteriaForm: FormGroup;
  queryModel: QueryModel = {
    queryName: 'qn_1',
    variable: 'qv_1',
    description: 'q_desc_1',
    numberOfEntries: 10,
    resourceType: 'res_type_1',
    siteArea: 'sa_1',
    nestedLevel: 1,
    category: 'cat_1',
    publishDateAfter: new Date(),
    publishDateBefore: new Date()
  }
  constructor(private formBuilder: FormBuilder,) { }

  ngOnInit() {
    this.searchCriteriaForm = this.createSearchCriteriaForm();
  }

  saveSearchCriteria() {
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
      resourceType      : [this.queryModel.resourceType],
      siteArea          : [this.queryModel.siteArea],
      category          : [this.queryModel.category],
      publishDateAfter  : [this.queryModel.publishDateAfter],
      publishDateBefore : [this.queryModel.publishDateBefore]
  });
  }
}
