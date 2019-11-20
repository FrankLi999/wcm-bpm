import { Component } from '@angular/core';
import { WcmConfigService } from 'bpw-wcm-service';
import { WcmConfigurableComponent } from '../../components/wcm-configurable.component';
@Component({
  selector: 'site-explorer',
  templateUrl: './site-explorer.component.html',
  styleUrls: ['./site-explorer.component.scss']
})
export class SiteExplorerComponent extends WcmConfigurableComponent {
  constructor(private wcmConfigService: WcmConfigService) {
    super(wcmConfigService);
  }
}