import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-calculator',
  templateUrl: './calculator.component.html',
  styleUrl: './calculator.component.css'
})
export class CalculatorComponent {

  @Input()
  title:string = '';

  number01:number = 5;

  @Output() 
  emitter: EventEmitter<number>= new EventEmitter<number>();

  onClick(){
    this.emitter.emit(this.number01);
  }
 
}
