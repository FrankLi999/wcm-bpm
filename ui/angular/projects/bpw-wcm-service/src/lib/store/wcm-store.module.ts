import { StoreModule } from "@ngrx/store";
import { NgModule } from "@angular/core";
import { EffectsModule } from "@ngrx/effects";

import { wcmAppFeatureKey, reducers } from "./reducers/wcm-authoring.reducer";
import { ContentAreaLayoutEffects } from "./effects/content-area-layout.effects";
import { WcmSystemEffects } from "./effects/wcm-system.effects";
import { WcmLibraryEffects } from "./effects/wcm-library.effects";
import { QueryEffects } from "./effects/query.effects";
import { WorkflowEffects } from "./effects/workflow.effects";
import { ValidationRuleEffects } from "./effects/validation-rule.effects";
import { SiteConfigEffects } from "./effects/site-config.effects";

@NgModule({
  imports: [
    StoreModule.forFeature(wcmAppFeatureKey, reducers),
    EffectsModule.forFeature([
      WcmSystemEffects,
      WcmLibraryEffects,
      ContentAreaLayoutEffects,
      QueryEffects,
      WorkflowEffects,
      ValidationRuleEffects,
      SiteConfigEffects
    ])
  ]
})
export class WcmAppStoreModule {}
