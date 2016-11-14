package in.appdirect.movierental.repository;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import in.appdirect.movierental.builder.MovieBuilder;
import in.appdirect.movierental.builder.RentedMovieBuilder;
import in.appdirect.movierental.model.Movie;
import in.appdirect.movierental.model.Order;
import in.appdirect.movierental.model.RentedMovie;
import in.appdirect.movierental.vo.Cart;

public class MovieRepositoryTest {
	
	private DatabaseTemplate databaseTemplate;
	
	private MovieRepository movieRepository;

	@Before
	public void setUp() throws Exception {
		movieRepository = new MovieRepository();
		databaseTemplate = new DatabaseTemplate();
		movieRepository.setDatabaseTemplate(databaseTemplate);
	}
	
	@Test
	public void testShouldCheckMovieService_GetMovieListAndReturnAllMovies() {
		
		List<Movie> actualMovieVoList = movieRepository.getMovieList();
		
		assertEquals(3, actualMovieVoList.size());
	}
	
	@Test
	public void testShouldCheckMovieService_getMovieDetailsForMovieNameAndReturnMovieDetails() {
		Movie movie = new MovieBuilder().withId("12345").withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		
		Movie obtainedMovie = movieRepository.getMovieDetails("12345");
		
		assertEquals(movie, obtainedMovie);
		
	}
	
	@Test
	public void testShouldCheckMovieService_placeOrder() {
		Movie movie = new MovieBuilder().withId("12345").withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		RentedMovie orderDetails = new RentedMovieBuilder().withMovie(movie).withQuantity(2l).build();
		List<RentedMovie> orderDetailsList = new ArrayList<RentedMovie>();
		orderDetailsList.add(orderDetails);
		Cart cart = new Cart();
		cart.getRentedMovies().add(orderDetails);
		
		movieRepository.placeOrder(new Order(orderDetailsList));
		
		 assertEquals(1, databaseTemplate.getOrders().size());
		 assertEquals(1, databaseTemplate.getOrders().get(0).getRentedMovies().size());
	}
}
