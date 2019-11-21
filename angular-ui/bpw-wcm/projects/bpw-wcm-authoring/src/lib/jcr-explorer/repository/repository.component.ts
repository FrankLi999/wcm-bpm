import { Component, OnInit, Input, ViewEncapsulation } from '@angular/core';
import { Repository } from 'bpw-wcm-service';

@Component({
  selector: 'repository',
  templateUrl: './repository.component.html',
  styleUrls: ['./repository.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RepositoryComponent implements OnInit {

  @Input() repository: Repository;
  
  constructor() { }

  ngOnInit() {
  }

}
