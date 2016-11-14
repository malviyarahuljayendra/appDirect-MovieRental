package in.appdirect.movierental.builder;

import in.appdirect.movierental.model.Movie;
import in.appdirect.movierental.model.RentedMovie;

public class RentedMovieBuilder {

	private Movie movie;
	
	private Long quantity;
	
	public RentedMovie build()
    {
		RentedMovie order = new RentedMovie();
		order.setMovie(movie);
		order.setQuantity(quantity);
        return order;
    }
	
	public RentedMovieBuilder reset()
    {
		this.movie = null;
		this.quantity = null;
        return this;
    }
 
    public RentedMovieBuilder withMovie(Movie movie)
    {
        this.movie = movie;
        return this;
    }
    
    public RentedMovieBuilder withQuantity(Long quantity)
    {
        this.quantity = quantity;
        return this;
    }
    
}
