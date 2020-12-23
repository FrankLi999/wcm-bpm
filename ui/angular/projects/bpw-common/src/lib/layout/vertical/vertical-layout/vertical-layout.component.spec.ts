import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VerticalLayoutComponent } from './vertical-layout.component';

describe('VerticalLayoutComponent', () => {
  let component: VerticalLayoutComponent;
  let fixture: ComponentFixture<VerticalLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VerticalLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VerticalLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
