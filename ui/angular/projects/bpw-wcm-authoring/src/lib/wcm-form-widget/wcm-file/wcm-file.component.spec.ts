import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WcmFileComponent } from './wcm-file.component';

describe('WcmFileComponent', () => {
  let component: WcmFileComponent;
  let fixture: ComponentFixture<WcmFileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WcmFileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WcmFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
