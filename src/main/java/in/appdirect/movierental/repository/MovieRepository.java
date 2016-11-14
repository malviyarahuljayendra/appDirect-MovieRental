package in.appdirect.movierental.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.appdirect.movierental.model.Movie;
import in.appdirect.movierental.model.Order;

@Component
public class MovieRepository {
	
	@Autowired
	private DatabaseTemplate databaseTemplate;
	
	public void setDatabaseTemplate(DatabaseTemplate databaseTemplate) {
		this.databaseTemplate = databaseTemplate;
	}

	public List<Movie> getMovieList() {
		return (List<Movie>) databaseTemplate.findAll(Movie.class);
	}

	public Movie getMovieDetails(String movieId) {
		return (Movie) databaseTemplate.findById(movieId, Movie.class);
	}

	public void placeOrder(Order order) {
		databaseTemplate.save(order, Order.class);
	}
	
}
