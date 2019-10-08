import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditContentAreaLayoutComponent } from './edit-content-area-layout.component';

describe('EditContentAreaLayoutComponent', () => {
  let component: EditContentAreaLayoutComponent;
  let fixture: ComponentFixture<EditContentAreaLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditContentAreaLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditContentAreaLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
