import { Injectable } from '@angular/core';
import { Category} from './category';
import { CATEGORIES } from './mock-categories';
import { Observable, of} from 'rxjs';
import { MessageService } from './message.service';


@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private messageService: MessageService) { }

  getCategories(): Observable<Category[]> {
  	this.messageService.add('CategoryService: fetched categories');
  	return of(CATEGORIES);
  }

  getCategory(id: number): Observable<Category> {
  	this.messageService.add(`CategoryService: fetched category id=${id}`);
  	return of (CATEGORIES.find(category => category.id === id));
  }
}
