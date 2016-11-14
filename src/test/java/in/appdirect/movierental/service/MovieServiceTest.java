package in.appdirect.movierental.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import in.appdirect.movierental.builder.MovieBuilder;
import in.appdirect.movierental.builder.RentedMovieBuilder;
import in.appdirect.movierental.model.Movie;
import in.appdirect.movierental.model.RentedMovie;
import in.appdirect.movierental.repository.MovieRepository;
import in.appdirect.movierental.vo.Cart;

public class MovieServiceTest {
	
	private MovieService movieService;
	
	private MovieRepository mockMovieRepository;

	@Before
	public void setUp() throws Exception {
		movieService = new MovieService();
		mockMovieRepository = mock(MovieRepository.class);
		movieService.setMovieRepository(mockMovieRepository);
	}
	
	@Test
	public void testShouldCheckMovieService_GetMovieListAndReturnAllMovies() {
		List<Movie> movieList = new ArrayList<Movie>();
		MovieBuilder movieBuilder = new MovieBuilder();
		for(long i=1; i<=5; i++)
			movieList.add(movieBuilder.withId(""+i).withName("Movie"+i).withActor("Actor"+i).withDate("0"+i+"-Jan-2016").withRent(100l*i).build());
		
		//Setting behaviours
		reset(mockMovieRepository);
		when(mockMovieRepository.getMovieList()).thenReturn(movieList);
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieRepository); 
		
		List<Movie> actualMovieVoList = movieService.getMovies();
		
		for(Movie actualMovie : actualMovieVoList) {
			if(!movieList.contains(actualMovie)) {
				fail("Movie - "+actualMovie.getName()+" not expected in the result");
			}
		}
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieRepository, times(1)).getMovieList();
	}
	
	@Test
	public void testShouldCheckMovieService_getMovieDetailsForMovieNameAndReturnMovieDetails() {
		Movie movie = new MovieBuilder().withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		
		//Setting behaviours
		reset(mockMovieRepository);
		when(mockMovieRepository.getMovieDetails("Movie1")).thenReturn(movie);
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieRepository); 
		
		Movie obtainedMovie = movieService.getMovie("Movie1");
		
		assertEquals(movie, obtainedMovie);
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieRepository, times(1)).getMovieDetails("Movie1");
	}
	
	@Test
	public void testShouldCheckMovieService_rentMovieForExistingMovieNameAndReturnOrderDetailsList() {
		Movie movie = new MovieBuilder().withId("12345").withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		RentedMovie orderDetails = new RentedMovieBuilder().withMovie(movie).withQuantity(2l).build();
		List<RentedMovie> orderDetailsList = new ArrayList<RentedMovie>();
		orderDetailsList.add(orderDetails);
		Cart cart = new Cart();
		cart.getRentedMovies().add(orderDetails);
		
		//Setting behaviours
		reset(mockMovieRepository);
		when(mockMovieRepository.getMovieDetails("12345")).thenReturn(movie);
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieRepository); 
		
		 movieService.rentMovie("12345", cart);
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieRepository, times(1)).getMovieDetails("12345");
		 
		 assertEquals(3, cart.getCartItemsCount());
	}
	
	@Test
	public void testShouldCheckMovieService_rentMovieForNewMovieNameAndReturnOrderDetailsList() {
		Movie movie = new MovieBuilder().withId("12345").withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		List<RentedMovie> orderDetailsList = new ArrayList<RentedMovie>();
		Cart cart = new Cart();
		
		//Setting behaviours
		reset(mockMovieRepository);
		when(mockMovieRepository.getMovieDetails("12345")).thenReturn(movie);
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieRepository); 
		
		 movieService.rentMovie("12345", cart);
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieRepository, times(1)).getMovieDetails("12345");
		 
		 assertEquals(1, cart.getCartItemsCount());
	}
	
	@Test
	public void testShouldCheckMovieService_placeOrderAndReturnNothing() {
		
		Movie movie = new MovieBuilder().withId("12345").withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		RentedMovie orderDetails = new RentedMovieBuilder().withMovie(movie).withQuantity(2l).build();
		List<RentedMovie> orderDetailsList = new ArrayList<RentedMovie>();
		orderDetailsList.add(orderDetails);
		Cart cart = new Cart();
		cart.getRentedMovies().add(orderDetails);
		
		//Setting behaviours
		reset(mockMovieRepository);
		doNothing().when(mockMovieRepository).placeOrder(any());
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieRepository); 
		
		 movieService.placeOrder(cart);
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieRepository, times(1)).placeOrder(any());
		 
		 assertEquals(0, cart.getCartItemsCount());
	}
	
	@Test
	public void testShouldCheckMovieService_removeMovieFromCartAndDoNothingWhenQuantityNotSpecified() {
		Movie movie = new MovieBuilder().withId("12345").withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		RentedMovie orderDetails = new RentedMovieBuilder().withMovie(movie).withQuantity(2l).build();
		List<RentedMovie> orderDetailsList = new ArrayList<RentedMovie>();
		orderDetailsList.add(orderDetails);
		Cart cart = new Cart();
		cart.getRentedMovies().add(orderDetails);
		
		//Setting behaviours
		reset(mockMovieRepository);
		when(mockMovieRepository.getMovieDetails("12345")).thenReturn(movie);
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieRepository); 
		
		 movieService.removeMovieFromCart("12345", null, cart);
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieRepository, times(1)).getMovieDetails("12345");
		 
		 assertEquals(0, cart.getCartItemsCount());
	}
	
	@Test
	public void testShouldCheckMovieService_removeMovieFromCartAndDoNothingWhenQuantitySpecified() {
		Movie movie = new MovieBuilder().withId("12345").withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		RentedMovie orderDetails = new RentedMovieBuilder().withMovie(movie).withQuantity(2l).build();
		List<RentedMovie> orderDetailsList = new ArrayList<RentedMovie>();
		orderDetailsList.add(orderDetails);
		Cart cart = new Cart();
		cart.getRentedMovies().add(orderDetails);
		
		//Setting behaviours
		reset(mockMovieRepository);
		when(mockMovieRepository.getMovieDetails("12345")).thenReturn(movie);
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieRepository); 
		
		 movieService.removeMovieFromCart("12345", 1l, cart);
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieRepository, times(1)).getMovieDetails("12345");
		 
		 assertEquals(1, cart.getCartItemsCount());
	}
	
//	
//	@Test
//	public void testShouldCheckMovieService_getCartItemsCountAndReturnCount() {
//		
//		//Setting behaviours
//		reset(mockMovieRepository);
//		when(mockMovieRepository.getCartItemsCount()).thenReturn(5l);
//		
//		//create inOrder object passing any mocks that need to be verified in order
//		 InOrder inOrder = inOrder(mockMovieRepository); 
//		
//		assertEquals(5l,  movieService.getCartItemsCount());
//		
//		//Verifying that actual behavior is getting called on tested method
//		// can use - times(int), atLeast, atMost, never etc
//		 inOrder.verify(mockMovieRepository, times(1)).getCartItemsCount();
//	}
}
