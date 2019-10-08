import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewContentItemComponent } from './new-content-item.component';

describe('NewContentItemComponent', () => {
  let component: NewContentItemComponent;
  let fixture: ComponentFixture<NewContentItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewContentItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewContentItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
