import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PageDesignerComponent } from './page-designer.component';

describe('LayoutsComponent', () => {
  let component: PageDesignerComponent;
  let fixture: ComponentFixture<PageDesignerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PageDesignerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PageDesignerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
