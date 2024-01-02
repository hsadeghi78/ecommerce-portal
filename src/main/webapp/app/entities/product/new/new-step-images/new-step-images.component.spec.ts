import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewStepImagesComponent } from './new-step-images.component';

describe('NewStepImagesComponent', () => {
  let component: NewStepImagesComponent;
  let fixture: ComponentFixture<NewStepImagesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewStepImagesComponent],
    });
    fixture = TestBed.createComponent(NewStepImagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
