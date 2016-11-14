package in.appdirect.movierental.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
	
	private String id;
	
	public Order(List<RentedMovie> rentedMovies) {
		super();
		this.rentedMovies = rentedMovies;
		this.id = ""+rentedMovies;
	}

	private List<RentedMovie> rentedMovies = new ArrayList<RentedMovie>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<RentedMovie> getRentedMovies() {
		return rentedMovies;
	}

	public void setRentedMovies(List<RentedMovie> rentedMovies) {
		this.rentedMovies = rentedMovies;
	}

	@Override
	public String toString() {
		return "Cart [rentedMovies=" + rentedMovies + "]";
	}

}
