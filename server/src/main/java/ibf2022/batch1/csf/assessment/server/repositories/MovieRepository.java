package ibf2022.batch1.csf.assessment.server.repositories;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mongodb.client.result.UpdateResult;

import ibf2022.batch1.csf.assessment.server.models.Comment;
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
		int commentCount = 0;

		MatchOperation matchTitle = Aggregation.match(criteria);

		ProjectionOperation projectField = Aggregation.project("comments")
			.andExclude("_id");

		Aggregation pipeline = Aggregation.newAggregation(matchTitle, projectField);

		AggregationResults<Document> results = 
			template.aggregate(pipeline, "comments", Document.class);

		List<Document> resultsDoc = results.getMappedResults();

		if (results.getMappedResults().size() != 0) {
			Document result = resultsDoc.get(0);

			System.out.println(result.toJson() + " DOCUMENT TO JSON");
			// List<Comment> commentList = result.getList("comments", Comment.class);
			JsonReader reader = Json.createReader(new StringReader(result.toJson()));
			JsonObject resultObject = reader.readObject();
			JsonArray jsonArr = resultObject.getJsonArray("comments");
			commentCount = jsonArr.size();

			// System.out.println(commentCount + "COMMENT Count");

		}

		return commentCount;
	}
// MONGO QUERY 1 --------------------
// I get the array then I get the size.
		// db.comments.aggregate([
		// {
		// 	$match: { "title": "Pretty Baby: Brooke Shields"}
		// },
		// {
		// 	$project: { _id: 0, comments: 1}
		// }
		// ])
// MONGO QUERY 1 END --------------------

// can also use this to return the count directly, but i didn't
// db.comments.aggregate([
// {
// 	$match: { "title": "Pretty Baby: Brooke Shields"}
// },
// {
// 	$project: { _id: 0, count: { $size: "$comments"}
// 	}
// }
// ])

	




	// TODO: Task 8
	// Write a method to insert movie comments comments collection
	public void postCommentByTitle(String title, Comment c) {
		Criteria criteria = Criteria.where("title").is(title);
		Query query = Query.query(criteria);
		Update updateOps = new Update().push("comments", c);
		UpdateResult updateResult = template.upsert(query, updateOps, "comments");
		if (updateResult == null) {
			System.out.println("not updated");
		}
		else {
			System.out.println(updateResult.getUpsertedId() + " document(s) updated..");
		}

	
	}	

	// Write the native mongo database query in the comment below
	// MONGO QUERY HERE ---------------------------------
	// db.comments.updateOne(
	// 	{"title": "Pretty Baby: Brooke Shields"},
	// 	 { $push: {comments: {
	// 		name: "john",
	// 		rating: 5,
	// 		comment: "good"
	// 	} } },
	// 	{ upsert: true}
	// )
	
		
}
