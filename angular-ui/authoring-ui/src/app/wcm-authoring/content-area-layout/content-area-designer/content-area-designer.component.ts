import { Component, OnInit, OnDestroy, ViewEncapsulation, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { fuseAnimations } from 'bpw-components';
import { Subscription } from 'rxjs';
@Component({
  selector: 'content-area-designer',
  templateUrl: './content-area-designer.component.html',
  styleUrls: ['./content-area-designer.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class ContentAreaDesignerComponent implements OnInit, OnDestroy {
  @Input() layoutName: string;
  @Input() editing: boolean = false;
  @Input() repository: string;
  @Input() workspace: string;
  @Input() library: string;
  sub: Subscription;
  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.sub = this.route.queryParams.subscribe(
      param => { 
        this.repository = param.repository;
        this.workspace = param.workspace;
        this.library = param.library;
        this.layoutName = param.layoutName;
        this.editing = param.editing;
    });
  }

  /**
    * On destroy
    */
   ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
