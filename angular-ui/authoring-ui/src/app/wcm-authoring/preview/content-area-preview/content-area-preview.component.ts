import { Component, OnInit, OnDestroy } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { ContentAreaLayout, WcmSystem, SiteArea } from '../../model';
import { select, Store } from '@ngrx/store';
import cloneDeep from 'lodash-es/cloneDeep';
import { Subject, Subscription, Observable } from 'rxjs';
import { takeUntil, switchMap, filter } from 'rxjs/operators';
import { RendererService } from '../renderer.service';
import * as fromStore from '../../store';
import { WcmService } from 'app/wcm-authoring/service/wcm.service';
@Component({
  selector: 'app-content-area-preview',
  templateUrl: './content-area-preview.component.html',
  styleUrls: ['./content-area-preview.component.scss']
})
export class ContentAreaPreviewComponent implements OnInit, OnDestroy {

  navigationId: string = 'bpm-home';
  siteArea: SiteArea;
  layout?: ContentAreaLayout;
  wcmSystem: WcmSystem;
  sub: Subscription;
  private unsubscribeAll: Subject<any>;
  
  constructor(
    private route: ActivatedRoute,
    private wcmService: WcmService,
    private store: Store<fromStore.WcmAppState>,
    private rendererService: RendererService
  ) { 
    this.unsubscribeAll = new Subject<any>();
  }
  ngOnInit() {
    this.rendererService.clearup();
    this.sub = this.route.queryParams.pipe(
      switchMap(param => this.getSiteArea(param)),
      filter(siteArea => siteArea != null),
      switchMap(siteArea => {
        this.navigationId = siteArea.navigationId;
        return this.store.pipe(
          takeUntil(this.unsubscribeAll),
          select(fromStore.getWcmSystem))
      })
    ).subscribe(
      (wcmSystem: WcmSystem) => {
        if (wcmSystem) {
          this.wcmSystem = wcmSystem;
          this.siteArea = this.wcmSystem.siteAreas[this.navigationId];  
          this.layout = cloneDeep(this.wcmSystem.contentAreaLayouts[this.siteArea.contentAreaLayout]||{});
          if (this.siteArea.siteAreaLayout) {
            this.layout.sidePane = cloneDeep(this.siteArea.siteAreaLayout.sidePane);
            this.layout.rows = cloneDeep(this.siteArea.siteAreaLayout.rows);
          }
        }
      },
      response => {
        console.log("getApplicationConfig call ended in error", response);
        console.log(response);
      },
      () => {
        console.log("getApplicationConfig observable is now completed.");
      }
    );
  }

  getSiteArea(param: any): Observable<SiteArea> {
    return this.wcmService.getSiteArea(param.repository, param.workspace, param.nodePath);
  }

  /**
    * On destroy
    */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.sub.unsubscribe();
  }

  leftSidePane(): boolean {
    return this.layout && this.layout.sidePane && this.layout.sidePane.left; 
  }

  rightSidePane(): boolean {
    return this.layout && this.layout.sidePane && (!this.layout.sidePane.left); 
  }
  
  sideRenderId(viewerIndex) {
    return `s_${viewerIndex}`;
  }

  contentRenderId(rowIndex, columnIndex, viewerIndex) {
    return `c_${rowIndex}_${columnIndex}_${viewerIndex}`;
  }
}
