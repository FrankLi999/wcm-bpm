import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormDesignerTreeComponent } from './form-designer-tree.component';

describe('FormDesignerTreeComponent', () => {
  let component: FormDesignerTreeComponent;
  let fixture: ComponentFixture<FormDesignerTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormDesignerTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormDesignerTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
