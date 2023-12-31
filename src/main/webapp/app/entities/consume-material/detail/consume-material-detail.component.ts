import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IConsumeMaterial } from '../consume-material.model';

@Component({
  standalone: true,
  selector: 'jhi-consume-material-detail',
  templateUrl: './consume-material-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ConsumeMaterialDetailComponent {
  @Input() consumeMaterial: IConsumeMaterial | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
