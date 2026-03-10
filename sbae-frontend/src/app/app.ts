import {ChangeDetectorRef, Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReactiveFormsModule, FormGroup, FormControl, Validators} from '@angular/forms';
import { IncidentService } from './services/incident.service';
import { Incident } from './models/incident.model';
import { SearchFilter } from './models/search-filter.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {

  // Initialisation du formulaire avec les 6 champs du SearchFilter
  searchForm = new FormGroup({
    title: new FormControl('', Validators.maxLength(100)),
    description: new FormControl('', Validators.maxLength(255)),
    severity: new FormControl(''),
    lastName: new FormControl('', Validators.maxLength(50)),
    firstName: new FormControl('', Validators.maxLength(50)),
    email: new FormControl('', [Validators.email])
  });

  results: Incident[] = [];
  searchDuration: number | null = null;
  loading: boolean = false;
  isPageInvalid: boolean = false;

  // Pagination
  totalElements: number = 0;
  totalPages: number = 0;
  currentPage: number = 0;
  pageSize: number = 10;
  pageSizes: number[] = [10, 20, 50, 100];

  constructor(private readonly incidentService: IncidentService,
              private readonly cdr: ChangeDetectorRef) {}

  onSearch(resetPage: boolean = true) {
    if (this.searchForm.invalid) {
      return;
    }

    if (resetPage) {
      this.currentPage = 0;
    }

    this.loading = true;
    const filters = this.searchForm.value as SearchFilter;

    // Appel au service pour les résultats et la mesure de performance
    this.incidentService.searchWithTiming(filters, this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.results = [...response.page.content];
        this.totalElements = response.page.totalElements;
        this.totalPages = response.page.totalPages;

        // Temps de calcul côté front
        this.searchDuration = response.duration;
        this.loading = false;
        // Force angular à détecter les changements suite à la réponse de l'api
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur API:', err);
        this.loading = false;
        // Forcer pour enlever le loader en cas d'erreur
        this.cdr.detectChanges();
      }
    });
  }

  changePage(delta: number) {
    this.currentPage += delta;
    this.onSearch(false); // false car on veut garder les filtres actuels
  }

  onPageSizeChange(event: any) {
    this.pageSize = +event.target.value;
    this.currentPage = 0;
    this.onSearch(false);
  }

  goToPage(event: any) {
    const pageIdx = Number.parseInt(event.target.value, 10) - 1;

    if (!Number.isNaN(pageIdx) && pageIdx >= 0 && pageIdx < this.totalPages) {
      this.isPageInvalid = false;
      this.currentPage = pageIdx;
      // On garde les filtres, on change juste la page
      this.onSearch(false);
    } else {
      this.isPageInvalid = true;
      // On remet la valeur correcte après 1.5 seconde
      setTimeout(() => {
        event.target.value = this.currentPage + 1;
        this.isPageInvalid = false;
        this.cdr.detectChanges();
      }, 1500);
    }
  }
}
