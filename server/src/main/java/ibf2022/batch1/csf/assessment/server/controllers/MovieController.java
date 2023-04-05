package ibf2022.batch1.csf.assessment.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ibf2022.batch1.csf.assessment.server.models.Comment;
import ibf2022.batch1.csf.assessment.server.models.Review;
import ibf2022.batch1.csf.assessment.server.services.MovieService;

@Controller
@CrossOrigin("*")
public class MovieController {

	@Autowired
	private MovieService movieSvc;

	// TODO: Task 3, Task 4, Task 8

	@GetMapping(path="api/search")
	public ResponseEntity<List<Review>> searchReviews(@RequestParam String query){

		List<Review> reviewList = movieSvc.searchReviews(query);

		// System.out.println(reviewList.toString() + "REVIEW LIST");
		if (reviewList.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}
		return new ResponseEntity<>(reviewList, HttpStatus.OK);
	}

	@PostMapping(path="api/comment")
	public ResponseEntity<String> postCommentByTitle(@RequestParam("title") String title,
	@RequestParam("name") String name, @RequestParam("rating") Integer rating,
	@RequestParam("comment") String comment){
		System.out.println("movie name: " + title);
		System.out.println("commenter: " + name);
		System.out.println("rating: " + rating);
		System.out.println("comment: " + comment);
	
		Comment c = new Comment();
		c.setComment(comment);
		c.setName(name);
		c.setRating(rating);
		
		movieSvc.postCommentByTitle(title, c);

		return null;
	}
}
