import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { LoginRequest } from '../interfaces/login-request.interface';
import { LoginResponse } from '../interfaces/login-response.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = environment.apiUrl;
  private storageKey = 'isLoggedIn'; // Clave para almacenar en localStorage
  private responseKey = 'loginResponse'; // Clave para almacenar el objeto LoginResponse
  private expirationKey = 'loginResponseExpiration'; // Clave para almacenar la fecha de expiración

  private expirationTimer: any; // Variable para almacenar el temporizador de expiración

  constructor(private http: HttpClient) {
    this.initExpirationTimer(); // Iniciar el temporizador de expiración al cargar el servicio
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, credentials)
      .pipe(
        tap((response: LoginResponse) => {
          // Calcular la fecha de expiración (1 hora desde ahora)
          const expiration = new Date();
          expiration.setHours(expiration.getHours() + 1); // Expira en 1 hora

          // Guardar el estado de inicio de sesión, el objeto LoginResponse y la fecha de expiración en localStorage
          localStorage.setItem(this.storageKey, 'true');
          localStorage.setItem(this.responseKey, JSON.stringify(response));
          localStorage.setItem(this.expirationKey, expiration.getTime().toString()); // Almacena el timestamp de expiración

          this.initExpirationTimer(); // Reiniciar el temporizador de expiración al iniciar sesión
        })
      );
  }

  // Método para verificar si el usuario ha iniciado sesión y si los datos no han expirado
  isLoggedIn(): boolean {
    const loggedIn = localStorage.getItem(this.storageKey) === 'true';
    const expiration = localStorage.getItem(this.expirationKey);
    if (loggedIn && expiration) {
      const expirationTime = parseInt(expiration, 10); // Convertir el timestamp almacenado a número entero
      return expirationTime > Date.now(); // Comprueba si la fecha de expiración es posterior al tiempo actual
    }
    return false; // Si no hay datos o han expirado, devuelve false
  }

  // Método para obtener el objeto LoginResponse almacenado en localStorage si no ha expirado
  getLoginResponse(): LoginResponse | null {
    if (this.isLoggedIn()) {
      const storedResponse = localStorage.getItem(this.responseKey);
      return storedResponse ? JSON.parse(storedResponse) : null;
    }
    return null;
  }

  // Método para cerrar sesión (eliminar los valores almacenados)
  logout(): void {
    localStorage.removeItem(this.storageKey);
    localStorage.removeItem(this.responseKey);
    localStorage.removeItem(this.expirationKey);
    this.clearExpirationTimer(); // Limpiar el temporizador de expiración al cerrar sesión
  }

  // Inicializa el temporizador de expiración para limpiar automáticamente los datos cuando expire la sesión
  private initExpirationTimer(): void {
    // Limpiar el temporizador si ya está activo
    this.clearExpirationTimer();

    // Obtener la fecha de expiración del localStorage
    const expiration = localStorage.getItem(this.expirationKey);
    if (expiration) {
      const expirationTime = parseInt(expiration, 10); // Convertir el timestamp almacenado a número entero

      // Calcular el tiempo restante hasta la expiración
      const expiresIn = expirationTime - Date.now();
      if (expiresIn > 0) {
        // Configurar el temporizador para limpiar los datos cuando expire la sesión
        this.expirationTimer = setTimeout(() => {
          this.logout(); // Llamar al método logout() cuando expire la sesión
        }, expiresIn);
      }
    }
  }

  // Limpiar el temporizador de expiración
  private clearExpirationTimer(): void {
    if (this.expirationTimer) {
      clearTimeout(this.expirationTimer);
      this.expirationTimer = null;
    }
  }
}
