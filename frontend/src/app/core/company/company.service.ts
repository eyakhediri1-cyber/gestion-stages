import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CompanyProfileRequest {
  companyName: string;
  industry: string;
  description: string;
  website: string;
  address: string;
  phone: string;
  logoUrl: string;
}

export interface CompanyProfileResponse
  extends CompanyProfileRequest {
  id: number;
  verified: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  private http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8080/api/companies';

  getMyProfile(): Observable<CompanyProfileResponse> {
    return this.http.get<CompanyProfileResponse>(
      `${this.apiUrl}/me`
    );
  }

  updateMyProfile(
    request: CompanyProfileRequest
  ): Observable<CompanyProfileResponse> {
    return this.http.put<CompanyProfileResponse>(
      `${this.apiUrl}/me`,
      request
    );
  }
}
