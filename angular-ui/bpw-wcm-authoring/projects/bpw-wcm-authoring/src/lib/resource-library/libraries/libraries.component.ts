import { Component } from '@angular/core';
import { WcmConfigService } from 'bpw-wcm-service';
import { WcmConfigurableComponent } from '../../components/wcm-configurable.component';
@Component({
  selector: 'resource-libraries',
  templateUrl: './libraries.component.html',
  styleUrls: ['./libraries.component.scss']
})
export class LibrariesComponent extends WcmConfigurableComponent {
  constructor(private wcmConfigService: WcmConfigService) {
    super(wcmConfigService);
  }
}