import { Component, EventEmitter, Output, OnDestroy } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Observable, Subject, filter, map, switchMap, takeUntil } from 'rxjs';

import { Book } from '../models/book';
import { BookService } from '../book.service';

type SearchType = 'keyword' | 'semantic';

@Component({
  selector: 'lms-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent implements OnDestroy {
  @Output() itemsFound = new EventEmitter<Book[]>();

  private submit$ = new Subject<void>();
  private destroy$ = new Subject<void>();

  searchForm = this.fb.group({
    query: this.fb.control('', { validators: [Validators.required], nonNullable: true }),
    searchType: this.fb.control<SearchType>('keyword', { nonNullable: true }),
    yearFrom: this.fb.control<number | null>(null),
    yearTo: this.fb.control<number | null>(null)
  });
  

  constructor(
    private bookService: BookService,
    private fb: FormBuilder
  ) {
    this.search()
      .pipe(takeUntil(this.destroy$))
      .subscribe(books => this.itemsFound.emit(books));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSearch(): void {
    this.submit$.next();
  }

  onEnter(event: Event): void {
    event.preventDefault();
    this.onSearch();
  }

  private search(): Observable<Book[]> {
    return this.submit$.pipe(
      map(() => {
        const query = this.searchForm.controls.query.value.trim();
        const type = this.searchForm.controls.searchType.value;
  
        const yearFrom = this.searchForm.controls.yearFrom.value;
        const yearTo = this.searchForm.controls.yearTo.value;
  
        return { query, type, yearFrom, yearTo };
      }),
      filter(({ query }) => query.length > 1),
      switchMap(({ query, type, yearFrom, yearTo }) =>
        this.bookService.search(query, type, yearFrom, yearTo)
      )
    );
  }
  
}
