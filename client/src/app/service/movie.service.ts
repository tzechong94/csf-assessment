import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  
  movieSearched: any;

  constructor(private http: HttpClient) { }

  getMoviesByName(searchInput: string) {
    const params = new HttpParams()
      .set("query", searchInput)

    return firstValueFrom(this.http.get("/api/search", { params }))
  }

  postComment(formData: FormData){
    return firstValueFrom(
      this.http.post("/api/comment", formData)
    )
  }
}
