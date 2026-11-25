import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';

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

export interface StudentProfileUpdate {
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

  private readonly apiUrl = 'http://localhost:8080/api/students';

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();

    return new HttpHeaders({
      Authorization: `Bearer ${token ?? ''}`
    });
  }

  getMyProfile(): Observable<StudentProfile> {
    return this.http.get<StudentProfile>(
      `${this.apiUrl}/me`,
      { headers: this.getHeaders() }
    );
  }

  updateMyProfile(
    profile: StudentProfileUpdate
  ): Observable<StudentProfile> {
    return this.http.put<StudentProfile>(
      `${this.apiUrl}/me`,
      profile,
      { headers: this.getHeaders() }
    );
  }
}
