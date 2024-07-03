import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterAccountComponent } from './filter-account.component';

describe('FilterAccountComponent', () => {
  let component: FilterAccountComponent;
  let fixture: ComponentFixture<FilterAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FilterAccountComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FilterAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
