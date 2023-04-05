package ibf2022.batch1.csf.assessment.server.repositories;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2022.batch1.csf.assessment.server.models.Review;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue.ValueType;

@Repository
public class MovieRepository {

	@Autowired
	private MongoTemplate template;

	private String apikey = "QKe1w5m2Vpz30mcyLTP4xdQGyCVDx5Y2";
    public static final String MOVIE_REVIEWS_API = "https://api.nytimes.com/svc/movies/v2/reviews/search.json";


	public List<Review> searchReviews(String query) {

		String url = UriComponentsBuilder.fromUriString(MOVIE_REVIEWS_API)
		.queryParam("query", query)
		.queryParam("api-key", apikey)
		.build()
		.toUriString();

		RequestEntity<Void> req = RequestEntity.get(url)
		.accept(MediaType.APPLICATION_JSON)
		.build();

		RestTemplate template = new RestTemplate();

		ResponseEntity<String> res = null;

		res = template.exchange(req, String.class);

		String payload = res.getBody();
        System.out.println("payload " + payload);
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject reviewResults = reader.readObject();

		if (reviewResults.get("results").getValueType() == ValueType.NULL) {
			return Collections.emptyList();
		} else {
			JsonArray jsonArr = reviewResults.getJsonArray("results");

			List<Review> resultList = jsonArr.stream()
								.map(r -> r.asJsonObject())
								.map(Review::toReview)
								.toList();
			
			for (Review r: resultList){
				int commentCount = countComments(r.getTitle());
				r.setCommentCount(commentCount);
			}
			
			return resultList;
	
		}
	}


	// TODO: Task 5
	// You may modify the parameter but not the return type
	// Write the native mongo database query in the comment below
	//
	public int countComments(String title) {
		Criteria criteria = Criteria.where("title").is(title);
		Query query = Query.query(criteria);
		List<String> comments = template.find(query, String.class, "comments");
		int commentCount = comments.size();
		return commentCount;
		// aggregation -> db.comments.aggregate([
		//   { $match: { title: "dogfather" } },
		//   { $project: { numComments: { $size: "$comments" } } }
		// ])
		// i get the array and find the size instead
		// db.comments.findOne({ title: "dogfather" }, { comments: 1, _id: 0 })
	}


	// TODO: Task 8
	// Write a method to insert movie comments comments collection
	// Write the native mongo database query in the comment below
	//
}
