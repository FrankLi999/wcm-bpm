import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription, Observable, of} from 'rxjs';
import { switchMap, map, filter } from 'rxjs/operators';
import { select, Store } from '@ngrx/store';
import { JsonForm, SiteArea } from '../../model';
import * as fromStore from '../../store';
import { WcmService } from 'app/wcm-authoring/service/wcm.service';

@Component({
  selector: 'app-site-area',
  templateUrl: './site-area.component.html',
  styleUrls: ['./site-area.component.scss']
})
export class SiteAreaComponent implements OnInit, OnDestroy {
  @Input() repository: string;
  @Input() workspace: string;
  @Input() nodePath: string;
  @Input() editing: boolean = false;
  @Input() siteArea: SiteArea;
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
      switchMap(param => this.getSiteArea(param)),
      filter(siteArea => siteArea != null),
      switchMap(siteArea => this.getJsonForms(siteArea))
    ).subscribe(jsonForm => this.jsonForm = jsonForm);
  }

  getSiteArea(param: any): Observable<SiteArea> {
    this.nodePath = param.nodePath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.editing = param.editing === 'true';
    if (this.editing) {
      return this.wcmService.getSiteArea(this.repository, this.workspace, this.nodePath);
    } else {
      return of({
        repository: param.repository,
        workspace: param.workspace,
        name: 'site area name'
      })
    }
  }
  getJsonForms(siteArea: SiteArea): Observable<JsonForm> {
    this.siteArea = siteArea;
    console.log('getsiteArea', siteArea);
    return this.store.pipe(
      select(fromStore.getJsonForms),
      map(jsonForms => {
        return jsonForms['bpwizard/default/system/siteAreaType']
      })
    );
  }

  /**
    * On destroy
    */
  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  createContent(formData: any) {
    const sa: SiteArea = {
      ...formData,
      repository: this.repository,
      nodePath: this.nodePath,
      workspace: this.workspace,
    }
    this.wcmService.createSiteArea(sa)
      .subscribe((event: any) => {
        console.log(event)
    });
  } 
}



