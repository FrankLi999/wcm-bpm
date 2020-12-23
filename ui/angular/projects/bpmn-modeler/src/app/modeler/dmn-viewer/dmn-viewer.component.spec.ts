import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DmnViewerComponent } from './dmn-viewer.component';

describe('DmnViewerComponent', () => {
  let component: DmnViewerComponent;
  let fixture: ComponentFixture<DmnViewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DmnViewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DmnViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
