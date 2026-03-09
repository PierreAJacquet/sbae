import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Incident } from '../models/incident.model';
import {SearchFilter} from '../models/search-filter.model';

@Injectable({ providedIn: 'root' })
export class IncidentService {

  constructor(private http: HttpClient) {}

  /**
   * Recherche tous les incidents correspondants aux filtres
   * et calcule le temps d'exécution.
   */
  searchWithTiming(filters: SearchFilter): Observable<{ data: Incident[], duration: number }> {
    const startTime = performance.now();

    // On envoie 'filters' directement comme corps de la requête POST
    return this.http.post<Incident[]>('/api/incidents/search-incidents', filters).pipe(
      map(data => {
        const endTime = performance.now();
        return {
          data: data,
          duration: (endTime - startTime) / 1000
        };
      })
    );
  }

  /**
   * Construit les paramètres de requête HTTP, uniquement pour les filtres actifs.
   */
  private buildParams(filters: any): HttpParams {
    let params = new HttpParams();

    if (filters) {
      Object.keys(filters).forEach(key => {
        const value = filters[key];
        // On n'ajoute que les filtres qui ont une valeur saisie
        if (value !== null && value !== undefined && value !== '') {
          params = params.set(key, value);
        }
      });
    }
    return params;
  }
}
