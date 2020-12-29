import { Component, OnInit, Input, ViewEncapsulation } from '@angular/core';
import { Repository } from 'bpw-wcm-service';
import { wcmAnimations } from 'bpw-common';
@Component({
  selector: 'repository',
  templateUrl: './repository.component.html',
  styleUrls: ['./repository.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : wcmAnimations
})
export class RepositoryComponent implements OnInit {

  @Input() repository: Repository;
  
  constructor() { }

  ngOnInit() {
  }

}
