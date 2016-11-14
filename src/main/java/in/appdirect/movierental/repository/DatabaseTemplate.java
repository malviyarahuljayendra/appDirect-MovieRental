package in.appdirect.movierental.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import in.appdirect.movierental.model.Movie;
import in.appdirect.movierental.model.Order;
import in.appdirect.movierental.model.RentedMovie;

/**
 * This class will be used to shadow actual templates (like SpringMongoTemplate
 * or SpringJDBCTemplate. Rather than actually inserting the data into db, I am
 * maintaining the data in static collection in this class.
 *
 */
@Component
public class DatabaseTemplate {

	private List<Movie> movieList;
	
	private List<RentedMovie> rentedMovieList;
	
	private List<Order> orders;
	
	public List<Order> getOrders() {
		return orders;
	}

	public DatabaseTemplate() {
		super();
		Movie movie1 = new Movie("12345", "Matrix", "Keanu Reaves", "01-Jan-2001", 500l);
		Movie movie2 = new Movie("56789", "The Last Samurai", "Tom Cruise", "01-Feb-2001", 2000l);
		Movie movie3 = new Movie("11225", "Rush Hour", "Jackie Chan", "01-Dec-2004", 600l);
		movieList = new ArrayList<Movie>(){{add(movie1);add(movie2);add(movie3);}};
		rentedMovieList = new ArrayList<>();
		orders = new ArrayList<>();
	}
	
	public DatabaseTemplate(List<Movie> movieList, List<RentedMovie> retedMovieList, List<Order> orders) {
		super();
		this.movieList = movieList;
		this.rentedMovieList = retedMovieList;
		this.orders = orders;
	}
	
	public List<?> findAll(Class classType) {
		if(classType.equals(Movie.class)) 
			return new ArrayList<Movie>(){{addAll(movieList);}};
		else
			return null;
	}
	
	public Object findById(String id, Class classType) {
		Object result = null;
		if(classType.equals(Movie.class)) 
			result = movieList.stream().filter(movie -> movie.getId().equalsIgnoreCase(id)).findFirst().get();
		else
			result = null;
		
		return result;
	}
	
	public void save(Object entity, Class classType) {
		if(classType.equals(Order.class))
			orders.add((Order) entity);
	}

}
