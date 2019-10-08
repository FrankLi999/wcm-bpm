import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription, Observable} from 'rxjs';
import { switchMap, map} from 'rxjs/operators';
import { select, Store } from '@ngrx/store';
import { JsonForm } from '../../model';
import * as fromStore from '../../store';
import { WcmService } from 'app/wcm-authoring/service/wcm.service';

@Component({
  selector: 'new-content-item',
  templateUrl: './new-content-item.component.html',
  styleUrls: ['./new-content-item.component.scss']
})
export class NewContentItemComponent implements OnInit, OnDestroy {
  @Input() authoringTemplate: string;
  @Input() repository: string;
  @Input() workspace: string;
  @Input() nodePath: string;
  sub: Subscription;
  jsonForm: JsonForm;
  jsonFormOptions: any = {
    addSubmit: true, // Add a submit button if layout does not have one
    debug: false, // Don't show inline debugging information
    loadExternalAssets: true, // Load external css and JavaScript for frameworks
    returnEmptyFields: false, // Don't return values for empty input fields
    setSchemaDefaults: true, // Always use schema defaults for empty fields
    defautWidgetOptions: { feedback: true }, // Show inline feedback icons
  };
  selectedFramework = 'material-design';
  selectedLanguage = 'en';
  constructor( 
    private wcmService: WcmService,
    private route: ActivatedRoute,
    private store: Store<fromStore.WcmAppState>) { }

  ngOnInit() {
    console.log('NewContentItemComponent forkJoin');
    this.sub = this.route.queryParams.pipe(
      switchMap(param => this.getJsonForms(param))
    ).subscribe(jsonForm => this.jsonForm = jsonForm);
  }

  getJsonForms(param: any): Observable<JsonForm> {
    this.authoringTemplate = param.authoringTemplate;
    this.nodePath = param.nodePath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    return this.store.pipe(
      select(fromStore.getJsonForms),
      map(jsonForms => {
        console.log(jsonForms[this.authoringTemplate]);
        return jsonForms[this.authoringTemplate]})
    );
  }

  /**
    * On destroy
    */
  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  createContent(formData: any) {
    console.log(formData)
    const contentItem = {
      contentElements: {...formData},
      repository: this.repository,
      nodePath: this.nodePath,
      workspace: this.workspace,
      authoringTemplate: this.authoringTemplate
    }
    console.log(contentItem);
    // this.wcmService.createContentItem(contentItem).subscribe((event: any) => {
    //     console.log(event)
    // });  
  } 
}
