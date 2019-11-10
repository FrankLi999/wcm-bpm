import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ExternalElementService {

  constructor() { }

  loaded = false;

  load(): void {
    if (this.loaded) return;
    const script = document.createElement('script');
    script.src = 'assets/external-elements.js';
    document.body.appendChild(script);
    this.loaded = true;
  }

}
