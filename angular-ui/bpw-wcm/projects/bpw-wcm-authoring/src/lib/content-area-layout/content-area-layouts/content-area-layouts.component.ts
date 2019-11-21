import { Component } from '@angular/core';
import { WcmConfigService } from 'bpw-wcm-service';
import { WcmConfigurableComponent } from '../../components/wcm-configurable.component';
@Component({
  selector: 'app-page-layouts',
  templateUrl: './content-area-layouts.component.html',
  styleUrls: ['./content-area-layouts.component.scss']
})
export class ContentAreaLayoutsComponent extends WcmConfigurableComponent {
  constructor(private wcmConfigService: WcmConfigService) {
    super(wcmConfigService);
  }
}