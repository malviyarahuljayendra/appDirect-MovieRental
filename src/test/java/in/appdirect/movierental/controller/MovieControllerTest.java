package in.appdirect.movierental.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.appdirect.movierental.builder.MovieBuilder;
import in.appdirect.movierental.builder.RentedMovieBuilder;
import in.appdirect.movierental.model.Movie;
import in.appdirect.movierental.model.RentedMovie;
import in.appdirect.movierental.service.MovieService;
import in.appdirect.movierental.vo.Cart;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@IntegrationTest
public class MovieControllerTest {

	private MockMvc mvc;
	
	private MovieService mockMovieService;
	
	private Cart cart;

	@Before
	public void setUp() throws Exception {
		MovieController movieController = new MovieController();
		mockMovieService = mock(MovieService.class);
		movieController.setMovieService(mockMovieService);
		cart = new Cart();
		movieController.setCart(cart);
		mvc = MockMvcBuilders.standaloneSetup(movieController).build();
		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testShouldCheckGetMovieListWithResponseAllMovieListAndCartItemsCount() throws Exception {
		List<Movie> movieList = new ArrayList<Movie>();
		MovieBuilder movieBuilder = new MovieBuilder();
		for(long i=1; i<=5; i++)
			movieList.add(movieBuilder.withName("Movie"+i).withActor("Actor"+i).withDate("0"+i+"-Jan-2016").withRent(100l*i).build());
		
		//Setting behaviours
		reset(mockMovieService);
		when(mockMovieService.getMovies()).thenReturn(movieList);
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieService); 
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/appDirect-movieRental/movies/onrent"))
				.andExpect(status().isOk())
				.andReturn();
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieService, times(1)).getMovies();
		 
		 assertEquals(200,  mvcResult.getResponse().getStatus());
		 assertEquals("application/json;charset=UTF-8",  mvcResult.getResponse().getContentType());
		 assertEquals(true, mvcResult.getResponse().getContentAsString().contains("Movie1"));
	}
	
	@Test
	public void testShouldCheckGetMovieDetailsForMovieNameWithResponseAsMovieDetailsAndCartItemsCount() throws Exception {
		Movie dummyMovie = new MovieBuilder().withId("12345").withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		
		//Setting behaviours
		reset(mockMovieService);
		
		when(mockMovieService.getMovie(eq("12345"))).thenReturn(dummyMovie);
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieService); 
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/appDirect-movieRental/movie/12345"))
				.andExpect(status().isOk())
				.andReturn();
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieService, times(1)).getMovie("12345");
		
		 Map<String, Object> result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Map.class);
		 assertEquals(200,  mvcResult.getResponse().getStatus());
		 assertEquals("application/json",  mvcResult.getResponse().getContentType());
		 assertEquals("{movieDetail={id=12345, name=Movie1, actor=Actor1, date=01-Jan-2016, rent=100}, cartItemsCount=0}", result.toString());
	}
	
	@Test
	public void testShouldCheckRentMovieDetailsForMovieNameWithResponseAsCartView() throws Exception {
		Movie dummyMovie = new MovieBuilder().withId("12345").withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		RentedMovie orderDetails = new RentedMovieBuilder().withMovie(dummyMovie).withQuantity(2l).build();
		List<RentedMovie> orderDetailsList = new ArrayList<RentedMovie>();
		orderDetailsList.add(orderDetails);
		//Setting behaviours
		reset(mockMovieService);
		doNothing().when(mockMovieService).rentMovie("12345", cart);
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieService); 
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/appDirect-movieRental/rent/12345"))
				.andExpect(status().isOk())
				.andReturn();
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieService, times(1)).rentMovie("12345", cart);
		
		 Map<String, Object> result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Map.class);
		 assertEquals(200,  mvcResult.getResponse().getStatus());
		 assertEquals("application/json",  mvcResult.getResponse().getContentType());
		 assertTrue(result.containsKey("RentedMovieList"));
		 assertTrue(result.containsKey("Total rent on rented movies"));
	}
	
	@Test
	public void testShouldCheckCartViewWithResponseAsCartView() throws Exception {
		Movie dummyMovie = new MovieBuilder().withName("Movie1").withActor("Actor1").withDate("01-Jan-2016").withRent(100l).build();
		RentedMovie orderDetails = new RentedMovieBuilder().withMovie(dummyMovie).withQuantity(2l).build();
		List<RentedMovie> orderDetailsList = new ArrayList<RentedMovie>();
		orderDetailsList.add(orderDetails);
		//Setting behaviours
		reset(mockMovieService);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/appDirect-movieRental/cart"))
				.andExpect(status().isOk())
				.andReturn();
		
		 Map<String, Object> result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Map.class);
		 assertEquals(200,  mvcResult.getResponse().getStatus());
		 assertEquals("application/json",  mvcResult.getResponse().getContentType());
		 assertTrue(result.containsKey("RentedMovieList"));
		 assertTrue(result.containsKey("Total rent on rented movies"));
	}
	
	@Test
	public void testShouldCheckePlaceOrderWithResponseAsEmptyCart() throws Exception {
		//Setting behaviours
		reset(mockMovieService);
		doNothing().when(mockMovieService).placeOrder(eq(cart));
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieService); 
		
		 MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/appDirect-movieRental/placeOrder"))
				.andExpect(status().isOk())
				.andReturn();
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieService, times(1)).placeOrder(cart);
		
		 Map<String, Object> result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Map.class);
		 assertEquals(200,  mvcResult.getResponse().getStatus());
		 assertEquals("application/json",  mvcResult.getResponse().getContentType());
		 assertTrue(result.containsKey("RentedMovieList"));
		 assertTrue(result.containsKey("Total rent on rented movies"));
	}
	
	@Test
	public void testShouldCheckRemoveMovieFromCartWithResponseAsEmptyCart() throws Exception {
		//Setting behaviours
		reset(mockMovieService);
		doNothing().when(mockMovieService).removeMovieFromCart(eq("12345"), eq(1l), eq(cart));
		
		//create inOrder object passing any mocks that need to be verified in order
		 InOrder inOrder = inOrder(mockMovieService); 
		
		 MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/appDirect-movieRental/cancel/movie/12345?quantity=1"))
				.andExpect(status().isOk())
				.andReturn();
		
		//Verifying that actual behavior is getting called on tested method
		// can use - times(int), atLeast, atMost, never etc
		 inOrder.verify(mockMovieService, times(1)).removeMovieFromCart(eq("12345"), eq(1l), eq(cart));
		
		 Map<String, Object> result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Map.class);
		 assertEquals(200,  mvcResult.getResponse().getStatus());
		 assertEquals("application/json",  mvcResult.getResponse().getContentType());
		 assertTrue(result.containsKey("RentedMovieList"));
		 assertTrue(result.containsKey("Total rent on rented movies"));
	}
	
}
