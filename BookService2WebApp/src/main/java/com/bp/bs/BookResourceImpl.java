package com.bp.bs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.model.wadl.ElementClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api( value = "/books", description = "Manage books" )
@Service("bookResourceImpl")
@Path("/books")
public class BookResourceImpl implements BookResource {
	private static final Logger logger = LoggerFactory
			.getLogger(BookResourceImpl.class);

	@Context
	private Request request;
	@Context
	private UriInfo uriInfo;
	
//	@GET
//	@ApiOperation(value="List matching books", notes="Gets a list of all books matching the search parms!", response = BooksState.class )
//	@ApiResponses(value= {@ApiResponse(code=200, message="Books found")})		
//	@Produces({"application/json","application/xml"})
//	//@ElementClass(response = BooksState.class)
//	@ElementClass(response = List.class)
//	public Response searchBooks(
//			@ApiParam( value = "Search for books containing this keyword", required = false ) 
//			@QueryParam("keyword") String keyword, 
//			@ApiParam( value = "Search for books with this publication date", required = false )
//			@QueryParam("pubdate") String pubDate) {
//		logger.trace("Entered searchBooks(...) ");
//		List<Book> books = BooksDB.getInstance().searchBooks(keyword, pubDate);
//		BooksState result = new BooksState();
//		for (Book book : books) {
//			BookState st = new BookState();
//			st.setIsbn(book.getIsbn());
//			st.setTitle(book.getTitle());
//			result.getBook().add(st);
//		}
//		//ResponseBuilder builder = Response.ok(result);
//		ResponseBuilder builder = Response.ok();
//		builder.entity(result.getBook());
//		//CacheController.setExpiry(builder);
//		return builder.build();
//	}

	@GET
	@ApiOperation(value="List matching books", notes="Gets a list of all books matching the search parms!", response = BooksState.class )
	@ApiResponses(value= {@ApiResponse(code=200, message="Books found")})		
	@Produces({"application/json","application/xml"})
	@ElementClass(response = BooksState.class)
	//@ElementClass(response = List.class)
	public Response searchBooks(
			@ApiParam( value = "Search for books containing this keyword", required = false ) 
			@QueryParam("keyword") String keyword, 
			@ApiParam( value = "Search for books with this publication date", required = false )
			@QueryParam("pubdate") String pubDate) {
		logger.trace("Entered searchBooks(...) ");
		List<Book> books = BooksDB.getInstance().searchBooks(keyword, pubDate);
		BooksState result = new BooksState();
		for (Book book : books) {
			BookState st = new BookState();
			st.setIsbn(book.getIsbn());
			st.setTitle(book.getTitle());
			result.getBook().add(st);
		}
		ResponseBuilder builder = Response.ok(result);
		//ResponseBuilder builder = Response.ok();
		//builder.entity(result.getBook());
		//CacheController.setExpiry(builder);
		return builder.build();
	}
	
	@GET
	@Path("/{isbn}")
	@Produces({"application/json","application/xml"})
	@ElementClass(response = BookState.class)
	public Response get(@PathParam("isbn") String isbn) {
		BooksDB bdb = BooksDB.getInstance();
		Book b = bdb.getBook(isbn);
		if (b != null) {
			logger.info("Book found:" + b);

			BookCacheController cacheController = new BookCacheController(b);

			EntityTag entityTag = new EntityTag(Long.toString(b.getVersion()));
			CacheControl cacheControl = new CacheControl();
			cacheControl.setMaxAge(cacheController.getMaxAge());

			// Eval the entity Tag and last modified date
			ResponseBuilder builder = request.evaluatePreconditions(
					b.getLastModified(), entityTag);
			if (builder == null) {
				BookState bst = createBookState(b);
				builder = Response.ok(bst);
				builder.lastModified(b.getLastModified());
				builder.tag(Long.toString(b.getVersion()));
			}
			builder.cacheControl(cacheControl);
			builder.expires(cacheController.getNextUpdate().getTime());
			return builder.build();
		}
		return Response.status(Status.NOT_FOUND).build();
	}

	@PUT
	@Path("/{isbn}")
	@Consumes("application/xml")
	@Produces("application/octet-stream")
	@ElementClass(request = BookState.class)
	public Response update(@PathParam("isbn") String isbn, BookState st) {
		BooksDB bdb = BooksDB.getInstance();
		boolean b = bdb.updateBook(st.getIsbn(), st.getTitle());
		if (b)
			return Response.status(Status.NO_CONTENT).build();
		else
			return Response.status(Status.NOT_FOUND).build();
	}

	@POST
	@Consumes("application/xml")
	@Produces("application/octet-stream")
	@ElementClass(request = BookState.class)
	public Response add(BookState st) {
		BooksDB bdb = BooksDB.getInstance();
		bdb.addBook(new Book(st.getIsbn(), st.getTitle()));
		UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
		uriBuilder.path(BookResource.class);
		ResponseBuilder builder = Response.created(uriBuilder.build(st
				.getIsbn()));
		return builder.build();
	}

	@DELETE
	@Path("/{isbn}")
	@Produces("application/octet-stream")
	public Response delete(@PathParam("isbn") String isbn) {
		BooksDB bdb = BooksDB.getInstance();
		boolean b = bdb.deleteBook(isbn);
		if (b)
			return Response.status(Status.NO_CONTENT).build();
		else
			return Response.status(Status.NOT_FOUND).build();
	}

	@Path("/{isbn}/reviews")
	public ReviewsResource getReviewsResource(@PathParam("isbn") String isbn) {
		BooksDB bookDB = BooksDB.getInstance();
		Book book = bookDB.getBook(isbn);
		if (book != null) {
			return new ReviewsResource(book);
		} else {
			Response response = Response.status(Status.NOT_FOUND).build();
			throw new WebApplicationException(response);
		}
	}

	private BookState createBookState(Book book) {
		BookState st = new BookState();
		st.setIsbn(book.getIsbn());
		st.setTitle(book.getTitle());
		return st;
	}
}
