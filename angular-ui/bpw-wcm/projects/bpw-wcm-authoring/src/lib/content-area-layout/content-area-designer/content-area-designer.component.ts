import { Component, OnInit, OnDestroy, ViewEncapsulation, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { wcmAnimations } from 'bpw-common';
import { Subscription } from 'rxjs';
import { WcmConfigService } from 'bpw-wcm-service';
import { WcmConfigurableComponent } from '../../components/wcm-configurable.component';
@Component({
  selector: 'content-area-designer',
  templateUrl: './content-area-designer.component.html',
  styleUrls: ['./content-area-designer.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : wcmAnimations
})
export class ContentAreaDesignerComponent extends WcmConfigurableComponent implements OnInit, OnDestroy {
  @Input() layoutName: string;
  @Input() editing: boolean = false;
  @Input() repository: string;
  @Input() workspace: string;
  @Input() library: string;
  sub: Subscription;
  constructor(private wcmConfigService: WcmConfigService,
      private route: ActivatedRoute) {
    super(wcmConfigService);
  }

  ngOnInit() {
    this.sub = this.route.queryParams.subscribe(
      param => { 
        this.repository = param.repository;
        this.workspace = param.workspace;
        this.library = param.library;
        this.layoutName = param.layoutName;
        this.editing = param.editing==='true';
    });
  }

  /**
    * On destroy
    */
   ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
