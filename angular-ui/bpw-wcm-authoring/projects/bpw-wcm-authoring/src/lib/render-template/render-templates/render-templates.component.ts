import { Component } from '@angular/core';
import { WcmConfigService } from '../../config/wcm-config.service';
import { WcmConfigurableComponent } from '../../components/wcm-configurable.component';
@Component({
  selector: 'render-templates',
  templateUrl: './render-templates.component.html',
  styleUrls: ['./render-templates.component.scss']
})
export class RenderTemplatesComponent extends WcmConfigurableComponent {
  constructor(private wcmConfigService: WcmConfigService) {
    super(wcmConfigService);
  }
}