import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';
import { Observable } from 'rxjs';

export interface StudentProfile {
  id: number;
  fullName: string;
  email: string;
  university: string;
  specialization: string;
  studyLevel: string;
  phone: string;
  cvUrl: string;
  linkedinUrl: string;
  githubUrl: string;
}

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private http = inject(HttpClient);
  private authService = inject(AuthService);

  private apiUrl = 'http://localhost:8080/api/students';

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.authService.getToken()}`
    });
  }

  getMyProfile(): Observable<StudentProfile> {
    return this.http.get<StudentProfile>(
      `${this.apiUrl}/me`,
      { headers: this.getHeaders() }
    );
  }

  updateMyProfile(
    profile: StudentProfile
  ): Observable<StudentProfile> {
    return this.http.put<StudentProfile>(
      `${this.apiUrl}/me`,
      profile,
      { headers: this.getHeaders() }
    );
  }
}
