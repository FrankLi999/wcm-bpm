import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DraftItemTreeComponent } from './draft-item-tree.component';

describe('DraftItemTreeComponent', () => {
  let component: DraftItemTreeComponent;
  let fixture: ComponentFixture<DraftItemTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DraftItemTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DraftItemTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
