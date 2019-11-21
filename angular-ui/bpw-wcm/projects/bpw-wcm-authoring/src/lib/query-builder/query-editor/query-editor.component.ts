import { Component, OnInit, Input } from '@angular/core';
import { WcmConfigService } from 'bpw-wcm-service';
import { WcmConfigurableComponent } from '../../components/wcm-configurable.component';
@Component({
  selector: 'query-editor',
  templateUrl: './query-editor.component.html',
  styleUrls: ['./query-editor.component.scss']
})
export class QueryEditorComponent  extends WcmConfigurableComponent implements OnInit {

  @Input() queryName: string;
  constructor(private wcmConfigService: WcmConfigService) { 
    super(wcmConfigService);
  }

  ngOnInit() {
  }

}
