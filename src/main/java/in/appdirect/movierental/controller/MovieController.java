package in.appdirect.movierental.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.appdirect.movierental.model.Movie;
import in.appdirect.movierental.service.MovieService;
import in.appdirect.movierental.vo.Cart;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "appDirect-movieRental")
public class MovieController {
	
	@Autowired 
	private MovieService movieService;
	
	public void setMovieService(MovieService movieService) {
		this.movieService = movieService;
	}

	@Autowired 
    private Cart cart;
	
	@ApiOperation(value = "Get List of Movies available for rent", nickname = "Available movies for rent")
    @RequestMapping(method = RequestMethod.GET,  path="/movies/onrent")
	public ResponseEntity<?> getMovieList() throws JsonProcessingException {
		List<Movie> movies = movieService.getMovies();
		Map response = new HashMap<String, Object>()
		{{put("movies", movies);put("movieCount", movies.size());put("message", new ObjectMapper().writeValueAsString(movies));}};
		return ResponseEntity.status(200).body(response);
	}
	
	@ApiOperation(value = "Get Movie details based on MovieId", nickname = "Movie details")
    @RequestMapping(method = RequestMethod.GET,  path="/movie/{movieId}")
	public ResponseEntity<?> getMovieDetails(@PathVariable String movieId) throws JsonProcessingException {
    	Movie movie = movieService.getMovie(movieId);
    	Map response = new HashMap<String, Object>(){{put("movieDetail", movie);put("cartItemsCount", cart.getCartItemsCount());}};
		return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
	}

	@ApiOperation(value = "Rent a movie based on Id", nickname = "Rent a movie")
	@RequestMapping(method = RequestMethod.PUT,  path="/rent/{movieId}")
	public ResponseEntity<?> rentMovie(@PathVariable String movieId) throws JsonProcessingException {
    	movieService.rentMovie(movieId, cart);
    	Map response = new HashMap<String, Object>()
		{{put("RentedMovieList", cart.getRentedMovies());put("Total rent on rented movies", cart.getTotalRent());put("RentedMovieList", cart.getRentedMovies());}};
		return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
	}
	
	@ApiOperation(value = "Remove a movie based on Id from cart", nickname = "Cancel a movie")
	@RequestMapping(method = RequestMethod.DELETE,  path="/cancel/movie/{movieId}")
	public ResponseEntity<?> removeMovieFromCart(@PathVariable String movieId,
			@RequestParam(name="quantity", required=false)	Long quantity) throws JsonProcessingException {
    	movieService.removeMovieFromCart(movieId, quantity, cart);
    	Map response = new HashMap<String, Object>()
		{{put("RentedMovieList", cart.getRentedMovies());put("Total rent on rented movies", cart.getTotalRent());put("RentedMovieList", cart.getRentedMovies());}};
		return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
	}

	@ApiOperation(value = "View added items on cart", nickname = "Cart view")
    @RequestMapping(method = RequestMethod.GET,  path="/cart")
	public ResponseEntity<?> cartView() throws JsonProcessingException {
		Map response = new HashMap<String, Object>()
		{{put("RentedMovieList", cart.getRentedMovies());put("Total rent on rented movies", cart.getTotalRent());}};
		return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
	}
	
	@ApiOperation(value = "Place final order and empty cart", nickname = "place order")
    @RequestMapping(method = RequestMethod.POST,  path="/placeOrder")
	public ResponseEntity<?> placeOrder() throws JsonProcessingException {
    	movieService.placeOrder(cart);
    	Map response = new HashMap<String, Object>()
		{{put("RentedMovieList", cart.getRentedMovies());put("Total rent on rented movies", cart.getTotalRent());put("RentedMovieList", cart.getRentedMovies());}};
		return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(response);
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
}
