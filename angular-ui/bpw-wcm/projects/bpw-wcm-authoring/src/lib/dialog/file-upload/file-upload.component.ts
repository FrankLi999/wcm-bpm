import { Component, OnInit, Inject, Input, Output, EventEmitter, ViewEncapsulation } from '@angular/core';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { 
  HttpClient,
  HttpRequest, 
  HttpEventType,
  HttpErrorResponse 
} from '@angular/common/http';
import { Subscription, of} from 'rxjs';
import { map, tap, last, catchError } from 'rxjs/operators';
import { ApiConfigService }  from 'bpw-rest-client';
export class FileUploadModel {
  data: File;
  relativePath: String;
  state: string;
  inProgress: boolean;
  progress: number;
  canRetry: boolean;
  canCancel: boolean;
  sub?: Subscription;
}

@Component({
  selector: 'file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss'],
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
export class FileUploadComponent implements OnInit {
  @Input() param = 'file';
  @Output() complete = new EventEmitter<string>();
  @Input() repositoryName = '';
  @Input() workspaceName = '';
  @Input() nodePath = '';
  files: Array<FileUploadModel> = [];

  constructor(
    private apiConfigService: ApiConfigService,
    private http: HttpClient) { }

  ngOnInit() {
  }

  onClick() {
    const fileUpload = document.getElementById('fileUpload') as HTMLInputElement;
    fileUpload.onchange = () => {
          for (let index = 0; index < fileUpload.files.length; index++) {
                const file = fileUpload.files[index];
                this.files.push({ data: file, relativePath: (file as any).webkitRelativePath, state: 'in', 
                  inProgress: false, progress: 0, canRetry: false, canCancel: true });
          }         
    };
    fileUpload.click();
  }

  cancelFile(file: FileUploadModel) {
    if (file.sub) {
      file.sub.unsubscribe();
    }
    this.removeFileFromArray(file);
  }

  retryFile(file: FileUploadModel) {
      this.uploadFile(file);
      file.canRetry = false;
  }

  hasFile() : boolean {
    return this.files.length > 0;
  }
  private uploadFile(file: FileUploadModel) {
      const fd = new FormData();
      fd.append(this.param, file.data);
      const req = new HttpRequest('POST', `${this.apiConfigService.apiConfig.apiBaseUrl}/modeshape/api/${this.repositoryName}/${this.workspaceName}/upload/${this.nodePath}/${file.relativePath.slice(file.relativePath.indexOf("/") + 1)}`, fd, {
            reportProgress: true
      });

      file.inProgress = true;
      file.sub = this.http.request(req).pipe(
            map(event => {
                  switch (event.type) {
                        case HttpEventType.UploadProgress:
                              file.progress = Math.round(event.loaded * 100 / event.total);
                              break;
                        case HttpEventType.Response:
                              return event;
                  }
            }),
            tap(message => { }),
            last(),
            catchError((error: HttpErrorResponse) => {
                  file.inProgress = false;
                  file.canRetry = true;
                  return of(`${file.data.name} upload failed.`);
            })
      ).subscribe(
            (event: any) => {
                  if (typeof (event) === 'object') {
                        this.removeFileFromArray(file);
                        // this.complete.emit(event.body);
                  }
            }
      );
  }

  uploadFiles() {
      const fileUpload = document.getElementById('fileUpload') as HTMLInputElement;
      fileUpload.value = '';

      this.files.forEach(file => {
            this.uploadFile(file);
      });
  }

  private removeFileFromArray(file: FileUploadModel) {
      const index = this.files.indexOf(file);
      if (index > -1) {
            this.files.splice(index, 1);
      }
  }
}