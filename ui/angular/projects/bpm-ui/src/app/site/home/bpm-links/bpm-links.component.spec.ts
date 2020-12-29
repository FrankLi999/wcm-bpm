import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpmLinksComponent } from './bpm-links.component';

describe('BpmLinksComponent', () => {
  let component: BpmLinksComponent;
  let fixture: ComponentFixture<BpmLinksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpmLinksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpmLinksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
