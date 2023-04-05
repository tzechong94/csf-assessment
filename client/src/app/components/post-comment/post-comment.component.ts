import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MovieService } from 'src/app/service/movie.service';

@Component({
  selector: 'app-post-comment',
  templateUrl: './post-comment.component.html',
  styleUrls: ['./post-comment.component.css']
})
export class PostCommentComponent implements OnInit {

  reviewTitle: string = ""

  form!: FormGroup
  name!: string
  rating!:number
  comment!: string
  constructor(private location: Location, private fb: FormBuilder, private router: Router,
     private activatedRoute: ActivatedRoute, private movieSvc: MovieService){}

  ngOnInit(): void {
    this.form = this.createForm()

    this.reviewTitle = this.activatedRoute.snapshot.params['title'].trim()
    console.log('review title:', this.reviewTitle)
  }

  postComment(){
    console.log('post comment')
    // const formData = new FormData();
    this.name = this.form.value.name
    this.rating = this.form.value.rating
    this.comment = this.form.value.comment
    this.movieSvc.postComment(this.reviewTitle, this.name, this.rating, this.comment)
    // formData.set("title", this.reviewTitle)
    // formData.set("name", this.form.get("name")!.value)
    // formData.set("rating", this.form.get("rating")!.value)
    // formData.set("comment", this.form.get("comment")!.value)
    // this.movieSvc.postComment(formData)
    this.location.back()


  }


  createForm() {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.minLength(3), Validators.required, this.noWhitespaceValidator]),
      rating: this.fb.control<number>(1, [Validators.required, Validators.min(1), Validators.max(5), Validators.pattern(/^[1-5]\d*$/)
    ]),
      comment: this.fb.control<string>('', [Validators.required]),
    })
  }

  public noWhitespaceValidator(control: FormControl) {
    const isSpace = (control.value || '').match(/\s/g);
    return isSpace ? {'whitespace': true} : null;
}


}
