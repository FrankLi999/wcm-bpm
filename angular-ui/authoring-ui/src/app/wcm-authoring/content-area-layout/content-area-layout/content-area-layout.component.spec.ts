import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentAreaLayoutComponent } from './content-area-layout.component';

describe('ContentAreaLayoutComponent', () => {
  let component: ContentAreaLayoutComponent;
  let fixture: ComponentFixture<ContentAreaLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentAreaLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentAreaLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
