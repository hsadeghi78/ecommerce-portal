import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewProductStepperComponent } from './new-product-stepper.component';

describe('NewProductStepperComponent', () => {
  let component: NewProductStepperComponent;
  let fixture: ComponentFixture<NewProductStepperComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewProductStepperComponent],
    });
    fixture = TestBed.createComponent(NewProductStepperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
