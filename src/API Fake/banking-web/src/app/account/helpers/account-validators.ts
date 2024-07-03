import { AbstractControl, ValidationErrors } from "@angular/forms";

export const accountNumberValidator= (control: AbstractControl): ValidationErrors | null => {
   const accountNumber: string = control.value;

   if (!/^\d{10}$/.test(accountNumber)) {
        return { invalidAccountNumber: true };
   }

   return null;
}


export const amountValidator = (control: AbstractControl): ValidationErrors | null => {
    const amount: number = parseFloat(control.value);
    
    if (isNaN(amount) || amount <= 0) { // Verifica si el valor es NaN o si es negativo
      return { invalidAmount: true };
    }
    return null;
};