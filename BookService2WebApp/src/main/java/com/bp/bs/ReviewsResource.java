package com.bp.bs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class ReviewsResource {
	//@Context private UriInfo uriInfo;
	private Book book;

	public ReviewsResource(Book book) {
		this.book = book;
	}

	@GET
	public Response getReviews(@Context UriInfo uriInfo) {
		ReviewsState result = new ReviewsState();
		int index = 0;
		for (Review r : book.getReviews()) {
			ReviewRef ref = new ReviewRef();
			ref.setSummary(r.getText().split(" ")[0] + "...");
			UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			builder.path(ReviewsResource.class, "getReview");
			ref.setUrl(builder.build(index).toString());
			result.getReviewRef().add(ref);
			index++;
		}
		ResponseBuilder builder = Response.ok(result);
		//CacheController.setExpiry(builder);
		return builder.build();
	}

	@Path("{index}")
	@GET
	public Response getReview(@PathParam("index") int index) {
		try {
			Review review = book.getReviews().get(index);
			ReviewState st = new ReviewState();
			st.setBy(review.getBy());
			st.setText(review.getText());
			ResponseBuilder builder = Response.ok(st);
			//CacheController.setExpiry(builder);
			return builder.build();
		} catch (IndexOutOfBoundsException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
}
