import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthRequest, AuthResponse, Profile, SignupRequest } from '../interfaces/auth.interface';

import { map } from 'rxjs';
import { environment } from '../../../environments/environment';

const authKey = 'banking_auth';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private _auth?: AuthResponse;

  constructor(
    private http: HttpClient
  ) {
    const authString = localStorage.getItem(authKey);

    if (authString) {
      this._auth = JSON.parse(authString);
    }
  }

  authenticate(authRequest: AuthRequest) {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/token`, authRequest)
      .pipe(
        map(response => {
          localStorage.setItem(authKey, JSON.stringify(response));
          this._auth = response;
          return response.user;
        })
      );
  }

  get user() {
    return this._auth?.user;
  }

  get token() {
    return this._auth?.token;
  }

  logout() {
    localStorage.removeItem(authKey);
    this._auth = undefined;
  }

  signup(signupRequest: SignupRequest) {
    return this.http.post<Profile>(`${environment.apiUrl}/users/signup`, signupRequest);
  }
}
