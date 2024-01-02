import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewStepGeneralInfoComponent } from './new-step-general-info.component';

describe('NewStepGeneralInfoComponent', () => {
  let component: NewStepGeneralInfoComponent;
  let fixture: ComponentFixture<NewStepGeneralInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewStepGeneralInfoComponent],
    });
    fixture = TestBed.createComponent(NewStepGeneralInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
