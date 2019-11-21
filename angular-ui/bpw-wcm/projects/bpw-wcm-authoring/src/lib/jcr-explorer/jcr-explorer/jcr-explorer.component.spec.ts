import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JcrExplorerComponent } from './jcr-explorer.component';

describe('JcrExplorerComponent', () => {
  let component: JcrExplorerComponent;
  let fixture: ComponentFixture<JcrExplorerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JcrExplorerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JcrExplorerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
