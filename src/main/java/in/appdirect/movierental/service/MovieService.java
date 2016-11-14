package in.appdirect.movierental.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.appdirect.movierental.model.Movie;
import in.appdirect.movierental.model.Order;
import in.appdirect.movierental.model.RentedMovie;
import in.appdirect.movierental.repository.MovieRepository;
import in.appdirect.movierental.vo.Cart;

@Service
public class MovieService {
	
	@Autowired 
	private MovieRepository movieRepository;
	
	public void setMovieRepository(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public List<Movie> getMovies() {
		return movieRepository.getMovieList();
	}
	
	public Movie getMovie(String movieId) {
		return movieRepository.getMovieDetails(movieId);
	}
	
	public void removeMovieFromCart(String movieId, Long quantity, Cart cart) {
		Movie movieDetails = movieRepository.getMovieDetails(movieId);
    	removeMovieFromCart(movieDetails, quantity, cart);
	}
	
	public void rentMovie(String movieId, Cart cart) {
		Movie movieDetails = movieRepository.getMovieDetails(movieId);
    	boolean isMoviePresentInCart = updateQuantity(movieDetails, cart);
    	addNewMovieInOrder(movieDetails, cart, isMoviePresentInCart);
	}

	public void placeOrder(Cart cart) {
		movieRepository.placeOrder(new Order(cart.getRentedMovies()));
		cart.getRentedMovies().clear();
	}
	
	private boolean removeMovieFromCart(Movie movieDetails, Long quantity, Cart cart) {
		Optional<RentedMovie> movie = cart.getRentedMovies().stream().
		filter(rentedMovie->rentedMovie.getMovie() != null && rentedMovie.getMovie().equals(movieDetails)).findFirst();
		if(movie.isPresent()) {
			if(quantity==null || quantity<0l || movie.get().getQuantity()<=quantity)
				cart.getRentedMovies().remove(movie.get());
			else
				movie.get().setQuantity(movie.get().getQuantity()-quantity);
		}
		return movie.isPresent();
	}
	
	private void addNewMovieInOrder(Movie movieDetails,
			Cart cart,
			boolean isMoviePresentInCart) {
		if(!isMoviePresentInCart) {
    		RentedMovie rentedMovie = new RentedMovie();
    		rentedMovie.setMovie(movieDetails);
    		cart.getRentedMovies().add(rentedMovie);
    	}
	}

	private boolean updateQuantity(Movie movieDetails, Cart cart) {
		boolean[] isMoviePresentInCart = {false};
		cart.getRentedMovies().forEach(rentedMovie->{
			if(rentedMovie.getMovie() != null && rentedMovie.getMovie().equals(movieDetails)) {
    			rentedMovie.setQuantity(rentedMovie.getQuantity()+1l);
    			isMoviePresentInCart[0] = true;
    		}
		});
		return isMoviePresentInCart[0];
	}

}
