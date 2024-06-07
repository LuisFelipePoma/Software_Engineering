import { Component, inject } from '@angular/core';
import { FormBuilder, FormControl, NonNullableFormBuilder, Validators } from '@angular/forms';
import { accountNumberValidator, amountValidator } from './transaction-custom-validator';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrl: './transaction.component.css'
})
export class TransactionComponent {

  //private readonly formBuilder = inject(FormBuilder);
  private readonly formBuilder = inject(NonNullableFormBuilder);

 formGroup = this.formBuilder.group({
    beneficiaryName:['', Validators.required],
    accountNumber:['', [Validators.required, accountNumberValidator]],
    amount:['', [Validators.required, amountValidator]]
  });


  get beneficiaryNameField() {
    return this.formGroup.controls.beneficiaryName;
  }

  get amountField() {
    return this.formGroup.controls.amount ;
  }
  
  get accountNumberField() {
    return this.formGroup.controls.accountNumber ;
  }
  
  submitTransfer(){
    if(this.formGroup.valid){
      console.log("Transferencia realizada");
    }else{  
      console.log("Formulario inválido");
    }
  }
  
}
