import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
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


  // postComment(formData: FormData){
  //   let options = {
  //     headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
  //   }
  
  //   return firstValueFrom(
  //     this.http.post("/api/comment", formData, options)
  //   )
  // }

  postComment(title:string, name:string, rating:number, comment:string){
    let body = new HttpParams()
            .set("title", title)
            .set("name", name)
            .set("rating", rating)
            .set("comment", comment)

    return firstValueFrom(
      this.http.post("/api/comment", body.toString(), 
      {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' 
      }
    })
    )
  }
}
