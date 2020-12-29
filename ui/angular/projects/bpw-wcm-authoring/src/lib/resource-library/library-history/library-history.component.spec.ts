import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LibraryHistoryComponent } from './library-history.component';

describe('LibraryHistoryComponent', () => {
  let component: LibraryHistoryComponent;
  let fixture: ComponentFixture<LibraryHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LibraryHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LibraryHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
