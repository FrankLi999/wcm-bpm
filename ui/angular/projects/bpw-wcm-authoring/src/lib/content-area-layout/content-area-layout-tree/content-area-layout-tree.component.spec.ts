import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentAreaLayoutTreeComponent } from './content-area-layout-tree.component';

describe('ContentAreaLayoutTreeComponent', () => {
  let component: ContentAreaLayoutTreeComponent;
  let fixture: ComponentFixture<ContentAreaLayoutTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentAreaLayoutTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentAreaLayoutTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
