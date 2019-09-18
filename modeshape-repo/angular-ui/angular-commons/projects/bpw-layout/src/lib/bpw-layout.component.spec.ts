import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BpwLayoutComponent } from './bpw-layout.component';

describe('BpwLayoutComponent', () => {
  let component: BpwLayoutComponent;
  let fixture: ComponentFixture<BpwLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BpwLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BpwLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
