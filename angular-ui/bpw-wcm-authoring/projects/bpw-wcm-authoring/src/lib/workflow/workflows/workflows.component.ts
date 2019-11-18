import { Component } from '@angular/core';
import { WcmConfigService } from '../../config/wcm-config.service';
import { WcmConfigurableComponent } from '../../components/wcm-configurable.component';
@Component({
  selector: 'app-workflows',
  templateUrl: './workflows.component.html',
  styleUrls: ['./workflows.component.scss']
})
export class WorkflowsComponent extends WcmConfigurableComponent {
  constructor(private wcmConfigService: WcmConfigService) {
    super(wcmConfigService);
  }
}