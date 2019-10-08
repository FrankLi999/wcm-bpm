import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewContentAreaLayoutComponent } from './new-content-area-layout.component';

describe('NewContentAreaLayoutComponent', () => {
  let component: NewContentAreaLayoutComponent;
  let fixture: ComponentFixture<NewContentAreaLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewContentAreaLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewContentAreaLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
