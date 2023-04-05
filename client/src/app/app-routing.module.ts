import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MovieReviewsListComponent } from './components/movie-reviews-list/movie-reviews-list.component';
import { SearchReviewComponent } from './components/search-review/search-review.component';

const routes: Routes = [
  { path: "", component: SearchReviewComponent },
  { path: "list", component: MovieReviewsListComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
