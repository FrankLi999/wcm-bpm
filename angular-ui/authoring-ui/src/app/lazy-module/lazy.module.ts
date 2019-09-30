import { NgModule } from '@angular/core';
import { AuthenticationModule, OAuth2Module} from 'bpw-auth';

@NgModule({
    imports  : [
        OAuth2Module,
        AuthenticationModule
    ]
})
export class LazyModule {
}