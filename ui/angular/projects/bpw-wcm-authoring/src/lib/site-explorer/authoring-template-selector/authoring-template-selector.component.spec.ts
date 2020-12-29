import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthoringTemplateSelectorComponent } from './authoring-template-selector.component';

describe('AuthoringTemplateSelectorComponent', () => {
  let component: AuthoringTemplateSelectorComponent;
  let fixture: ComponentFixture<AuthoringTemplateSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuthoringTemplateSelectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthoringTemplateSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
