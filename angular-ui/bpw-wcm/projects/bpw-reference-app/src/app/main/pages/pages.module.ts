import { NgModule } from '@angular/core';

import { LoginModule } from './authentication/login/login.module';
import { Login2Module } from './authentication/login-2/login-2.module';
import { RegisterModule } from './authentication/register/register.module';
import { Register2Module } from './authentication/register-2/register-2.module';
import { ForgotPasswordModule } from './authentication/forgot-password/forgot-password.module';
import { ForgotPassword2Module } from './authentication/forgot-password-2/forgot-password-2.module';
import { ResetPasswordModule } from './authentication/reset-password/reset-password.module';
import { ResetPassword2Module } from './authentication/reset-password-2/reset-password-2.module';
import { LockModule } from './authentication/lock/lock.module';
import { MailConfirmModule } from './authentication/mail-confirm/mail-confirm.module';
import { ComingSoonModule } from './coming-soon/coming-soon.module';
import { Error404Module } from './errors/404/error-404.module';
import { Error500Module } from './errors/500/error-500.module';
import { InvoiceModernModule } from './invoices/modern/modern.module';
import { InvoiceCompactModule } from './invoices/compact/compact.module';
import { MaintenanceModule } from './maintenance/maintenence.module';
import { PricingModule } from './pricing/pricing.module';
import { ProfileModule } from './profile/profile.module';
import { SearchClassicModule } from './search/classic/search-classic.module';
import { SearchModernModule } from './search/modern/search-modern.module';
import { FaqModule } from './faq/faq.module';
import { KnowledgeBaseModule } from './knowledge-base/knowledge-base.module';

@NgModule({
    imports: [
        // Authentication
        LoginModule,
        Login2Module,
        RegisterModule,
        Register2Module,
        ForgotPasswordModule,
        ForgotPassword2Module,
        ResetPasswordModule,
        ResetPassword2Module,
        LockModule,
        MailConfirmModule,

        // Coming-soon
        ComingSoonModule,

        // Errors
        Error404Module,
        Error500Module,

        // Invoices
        InvoiceModernModule,
        InvoiceCompactModule,

        // Maintenance
        MaintenanceModule,

        // Pricing
        PricingModule,

        // Profile
        ProfileModule,

        // Search
        SearchClassicModule,
        SearchModernModule,

        // Faq
        FaqModule,

        // Knowledge base
        KnowledgeBaseModule
    ]
})
export class PagesModule
{

}
