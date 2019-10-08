import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditContentItemComponent } from './edit-content-item.component';

describe('EditContentItemComponent', () => {
  let component: EditContentItemComponent;
  let fixture: ComponentFixture<EditContentItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditContentItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditContentItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
