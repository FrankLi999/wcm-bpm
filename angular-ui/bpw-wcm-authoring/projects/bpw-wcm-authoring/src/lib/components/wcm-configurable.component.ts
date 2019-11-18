import { WcmConfigService } from '../config/wcm-config.service';
export abstract class WcmConfigurableComponent {
  constructor(wcmConfigService: WcmConfigService) {
    wcmConfigService.setupConfig();
  }
}