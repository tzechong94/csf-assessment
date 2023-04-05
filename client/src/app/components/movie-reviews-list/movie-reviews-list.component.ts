import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Review } from 'src/app/model/model';
import { MovieService } from 'src/app/service/movie.service';

@Component({
  selector: 'app-movie-reviews-list',
  templateUrl: './movie-reviews-list.component.html',
  styleUrls: ['./movie-reviews-list.component.css']
})
export class MovieReviewsListComponent implements OnInit {

  constructor(private router:Router, private movieSvc: MovieService){}

  reviews: Review[] = [];
  noresult: string = "loading"

  ngOnInit(): void {
      this.movieSvc.getMoviesByName(this.movieSvc.movieSearched)
        .then(response => {
          this.reviews = response as Review[]

          // if (this.reviews.length === 0){
          //   this.noresult = "Your search produces no result"
          // } else {
            this.noresult = ""
          // }      
        })
        .catch((err)=>{
          console.log(err)
          this.noresult = "Your search produces no result"
          console.log(this.noresult)
        })

  }

  comment(reviewTitle: string) {
    this.router.navigate(["/comment/" + reviewTitle])
  }

}
