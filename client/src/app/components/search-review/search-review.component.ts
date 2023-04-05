import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MovieService } from 'src/app/service/movie.service';

@Component({
  selector: 'app-search-review',
  templateUrl: './search-review.component.html',
  styleUrls: ['./search-review.component.css']
})
export class SearchReviewComponent implements OnInit {

  form!: FormGroup
  
  constructor(private fb: FormBuilder, private router: Router
    ,private movieSvc: MovieService){}
  
  searchByMovie() {
    console.log('searching for movie: ', this.form.value.movie)
    this.movieSvc.movieSearched = this.form.value.movie
    this.router.navigate(["list"])
  }

  ngOnInit(): void {
      this.form = this.createForm()
  }

  createForm() {
    return this.fb.group({
      movie: this.fb.control<string>('', [Validators.minLength(2), Validators.required, this.noWhitespaceValidator])
    })
  }

  public noWhitespaceValidator(control: FormControl) {
    const isSpace = (control.value || '').match(/\s/g);
    return isSpace ? {'whitespace': true} : null;
}

}
