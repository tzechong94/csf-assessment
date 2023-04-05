package ibf2022.batch1.csf.assessment.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.batch1.csf.assessment.server.models.Comment;
import ibf2022.batch1.csf.assessment.server.models.Review;
import ibf2022.batch1.csf.assessment.server.repositories.MovieRepository;;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepo;

	// TODO: Task 4
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	public List<Review> searchReviews(String query) {

		return movieRepo.searchReviews(query);
	}

	public void postCommentByTitle(String title, Comment c) {
		movieRepo.postCommentByTitle(title, c);
	}




}
