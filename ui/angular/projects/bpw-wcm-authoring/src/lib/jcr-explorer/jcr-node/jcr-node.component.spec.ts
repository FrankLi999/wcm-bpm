import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JcrNodeComponent } from './jcr-node.component';

describe('JcrNodeComponent', () => {
  let component: JcrNodeComponent;
  let fixture: ComponentFixture<JcrNodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JcrNodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JcrNodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
