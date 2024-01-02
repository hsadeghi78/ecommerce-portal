import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewStepExtraInfoComponent } from './new-step-extra-info.component';

describe('NewStepExtraInfoComponent', () => {
  let component: NewStepExtraInfoComponent;
  let fixture: ComponentFixture<NewStepExtraInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewStepExtraInfoComponent],
    });
    fixture = TestBed.createComponent(NewStepExtraInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
