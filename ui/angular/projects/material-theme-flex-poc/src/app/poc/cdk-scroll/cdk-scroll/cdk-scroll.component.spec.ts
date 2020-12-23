import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CdkScrollComponent } from './cdk-scroll.component';

describe('CdkScrollComponent', () => {
  let component: CdkScrollComponent;
  let fixture: ComponentFixture<CdkScrollComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CdkScrollComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CdkScrollComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
