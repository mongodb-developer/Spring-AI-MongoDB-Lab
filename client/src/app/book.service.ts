import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Book } from './models/book';
import { Observable, map } from 'rxjs';
import { BookView } from './models/book-view';
import { URL } from './config';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  constructor(private http: HttpClient) { }

  list(limit = 12, skip = 0) {
    if (limit > 100) {
      limit = 100;
    }

    return this.http.get<Book[]>(`${URL}/books?limit=${limit}&skip=${skip}`)
      .pipe(
        map(books => books.map(book => new BookView(book)))
      )
  }

  getBook(isbn: string) {
    return this.http.get<Book>(`${URL}/books/${isbn}`)
      .pipe(
        map(book => new BookView(book))
      );
  }

  

  search(
    query: string,
    searchType: string | null = 'keyword',
    yearFrom?: number | null,
    yearTo?: number | null,
    limit = 12
  ): Observable<Book[]> {
    if (limit > 100) limit = 100;

    const type = searchType || 'keyword';

    let params = new HttpParams()
      .set('term', query)
      .set('type', type)
      .set('limit', String(limit));

    if (yearFrom != null) params = params.set('yearFrom', String(yearFrom));
    if (yearTo != null) params = params.set('yearTo', String(yearTo));

    return this.http.get<Book[]>(`${URL}/books/search`, { params });
  }

}
