import { Component, ViewEncapsulation } from '@angular/core';
import { WcmConfigService } from 'bpw-wcm-service';
import { fuseAnimations } from 'bpw-components';
import { WcmConfigurableComponent } from '../../components/wcm-configurable.component';
@Component({
  selector: 'site-explorer',
  templateUrl: './site-explorer.component.html',
  styleUrls: ['./site-explorer.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class SiteExplorerComponent extends WcmConfigurableComponent {
  constructor(private wcmConfigService: WcmConfigService) {
    super(wcmConfigService);
  }
}