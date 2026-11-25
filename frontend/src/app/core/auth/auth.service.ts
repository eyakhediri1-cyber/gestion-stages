import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export type UserRole = 'STUDENT' | 'COMPANY' | 'ADMIN';
export type RegistrationRole = 'STUDENT' | 'COMPANY';

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  role: RegistrationRole;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface UserResponse {
  id: number;
  fullName: string;
  email: string;
  role: UserRole;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  userId: number;
  fullName: string;
  email: string;
  role: UserRole;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/auth';

  private readonly tokenKey = 'stagia_token';
  private readonly userKey = 'stagia_user';

  register(request: RegisterRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(
      `${this.apiUrl}/register`,
      request
    );
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/login`,
      request
    ).pipe(
      tap(response => {
        sessionStorage.setItem(this.tokenKey, response.token);

        sessionStorage.setItem(
          this.userKey,
          JSON.stringify({
            userId: response.userId,
            fullName: response.fullName,
            email: response.email,
            role: response.role
          })
        );
      })
    );
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.tokenKey);
  }

  getCurrentUser(): Omit<AuthResponse, 'token' | 'tokenType'> | null {
    const user = sessionStorage.getItem(this.userKey);

    return user ? JSON.parse(user) : null;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    sessionStorage.removeItem(this.tokenKey);
    sessionStorage.removeItem(this.userKey);
  }
}
