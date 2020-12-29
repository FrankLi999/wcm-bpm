import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FlexLayoutModule } from "@angular/flex-layout";

import { CdkTableModule } from "@angular/cdk/table";
import { CdkTreeModule } from "@angular/cdk/tree";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { MatButtonModule } from "@angular/material/button";
import { MatButtonToggleModule } from "@angular/material/button-toggle";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatChipsModule } from "@angular/material/chips";
import { MatNativeDateModule } from "@angular/material/core";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatRadioModule } from "@angular/material/radio";
import { MatSelectModule } from "@angular/material/select";
import { MatSlideToggleModule } from "@angular/material/slide-toggle";
import { MatSliderModule } from "@angular/material/slider";
import { MatStepperModule } from "@angular/material/stepper";
import { MatTabsModule } from "@angular/material/tabs";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatMenuModule } from "@angular/material/menu";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTreeModule } from "@angular/material/tree";

import { JsonSchemaFormModule, SharedWidgetModule } from "@bpw/ajsf-core";

// import { AceEditorModule } from "ng2-ace-editor";

import { ComponentModule } from "../components/component.module";
import { CategoryFormWidgetComponent } from "./category-form-widget/category-form-widget.component";
import { SiteConfigFormWidgetComponent } from "./site-config-form-widget/site-config-form-widget.component";
import { SiteareaFormWidgetComponent } from "./sitearea-form-widget/sitearea-form-widget.component";
import { WorkflowFormWidgetComponent } from "./workflow-form-widget/workflow-form-widget.component";
import { ContentAreaLayoutFormWidgetComponent } from "./content-area-layout-form-widget/content-area-layout-form-widget.component";
import { WcmFileComponent } from './wcm-file/wcm-file.component';

@NgModule({
  declarations: [
    CategoryFormWidgetComponent,
    SiteConfigFormWidgetComponent,
    SiteareaFormWidgetComponent,
    WorkflowFormWidgetComponent,
    ContentAreaLayoutFormWidgetComponent,
    WcmFileComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,

    CdkTableModule,
    CdkTreeModule,
    ScrollingModule,

    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatNativeDateModule,
    MatRadioModule,
    MatSelectModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatStepperModule,
    MatTabsModule,
    MatTooltipModule,
    MatToolbarModule,
    MatMenuModule,
    MatToolbarModule,
    // AceEditorModule,
    SharedWidgetModule,
    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatNativeDateModule,
    MatRadioModule,
    MatSelectModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatStepperModule,
    MatTabsModule,
    MatTooltipModule,
    MatToolbarModule,
    MatTreeModule,
    MatMenuModule,
    MatToolbarModule,
    JsonSchemaFormModule,
    ComponentModule,
    // AceEditorModule,
  ],
  exports: [
    CategoryFormWidgetComponent,
    SiteConfigFormWidgetComponent,
    SiteareaFormWidgetComponent,
    WorkflowFormWidgetComponent,
    ContentAreaLayoutFormWidgetComponent,
  ],
})
export class WcmFormWidgetModule {}
