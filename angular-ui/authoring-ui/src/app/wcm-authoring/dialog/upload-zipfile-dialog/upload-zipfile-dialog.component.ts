import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-upload-zipfile-dialog',
  templateUrl: './upload-zipfile-dialog.component.html',
  styleUrls: ['./upload-zipfile-dialog.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations: [
    trigger('fadeInOut', [
          state('in', style({ opacity: 100 })),
          transition('* => void', [
                animate(300, style({ opacity: 0 }))
          ])
    ])
 ]
})
export class UploadZipfileDialogComponent implements OnInit {
  constructor(
    public matDialogRef: MatDialogRef<UploadZipfileDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { 
    }

  ngOnInit() {
  }

  onFileComplete(data: any) {
    console.log(data); // We just print out data bubbled up from event emitter.
    this.matDialogRef.close();
  }
}
