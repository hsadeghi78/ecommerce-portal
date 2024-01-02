import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewStepFinanceInfoComponent } from './new-step-finance-info.component';

describe('NewStepFinanceInfoComponent', () => {
  let component: NewStepFinanceInfoComponent;
  let fixture: ComponentFixture<NewStepFinanceInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewStepFinanceInfoComponent],
    });
    fixture = TestBed.createComponent(NewStepFinanceInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
