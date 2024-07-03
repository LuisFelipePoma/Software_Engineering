import { NgModule,LOCALE_ID } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';


import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';


import { registerLocaleData } from '@angular/common';
import localeEsPE from '@angular/common/locales/es-PE'; 
import { MaterialModule } from './modules/material/material.module';



registerLocaleData(localeEsPE);


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NavbarComponent,
    FooterComponent
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialModule,
    HttpClientModule,
    FormsModule
     
  ],
  providers: [
    provideAnimationsAsync(),
    { provide: LOCALE_ID, useValue: 'es-PE' } 
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
