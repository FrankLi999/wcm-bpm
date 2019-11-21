import { Component } from '@angular/core';
import { WcmConfigService } from 'bpw-wcm-service';
import { WcmConfigurableComponent } from '../../components/wcm-configurable.component';

@Component({
  selector: 'app-validation-rule',
  templateUrl: './validation-rule.component.html',
  styleUrls: ['./validation-rule.component.scss']
})
export class ValidationRuleComponent extends WcmConfigurableComponent {
  constructor(private wcmConfigService: WcmConfigService) {
    super(wcmConfigService);
  }
}