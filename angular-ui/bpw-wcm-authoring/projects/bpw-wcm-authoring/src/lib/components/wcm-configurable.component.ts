import { WcmConfigService } from 'bpw-wcm-service';
import { wcmAuthoringLayoutConfig } from '../config/wcm-authoring.config';
import { navigation } from '../navigation/navigation';

export abstract class WcmConfigurableComponent {
  constructor(wcmConfigService: WcmConfigService) {
    wcmConfigService.setupConfig(wcmAuthoringLayoutConfig, navigation);
  }
}