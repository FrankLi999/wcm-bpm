import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DmnDiagramTabsComponent } from './dmn-diagram-tabs.component';

describe('DmnDiagramTabsComponent', () => {
  let component: DmnDiagramTabsComponent;
  let fixture: ComponentFixture<DmnDiagramTabsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DmnDiagramTabsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DmnDiagramTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
