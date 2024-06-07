  import { Component } from '@angular/core';

  @Component({
    selector: 'app-card',
    templateUrl: './card.component.html',
    styleUrl: './card.component.css'
  })
  export class CardComponent {

  
    cardData = {
      imageUrl: 'https://via.placeholder.com/150',
      title: 'Card Title',
      description: 'Card Description'
    };
    


    onButtonClick(): void {
      // Aquí puedes agregar la lógica que desees al hacer clic en el botón
      console.log('Botón seleccionado');
    }

    
   
    
    
  }




