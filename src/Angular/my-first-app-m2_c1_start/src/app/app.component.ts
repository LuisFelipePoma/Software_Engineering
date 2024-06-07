import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  
  number01:number = 0;

  getNumber01(e:number){
    this.number01 = e;
  }
}





