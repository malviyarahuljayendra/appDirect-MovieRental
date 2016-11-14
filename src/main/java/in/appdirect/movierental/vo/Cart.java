package in.appdirect.movierental.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import in.appdirect.movierental.model.RentedMovie;


@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class Cart {
	
	private String id;
	
	private List<RentedMovie> rentedMovies = new ArrayList<RentedMovie>();
	
	private long totalRent;
	
	public long getCartItemsCount() {
		Long cartItemsCount = 0l;
		for(RentedMovie rentedMovie : rentedMovies) {
			cartItemsCount += rentedMovie.getQuantity();
		}
		return cartItemsCount;
	}

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

//	@Override
//	public String toString() {
//		return "Cart [rentedMovies=" + rentedMovies + "]";
//	}

	public long getTotalRent() {
		totalRent = 0l;
		for(RentedMovie rentedMovie : this.getRentedMovies()) {
    		totalRent += rentedMovie.getMovie().getRent()*rentedMovie.getQuantity();
    	}
		return totalRent;
	}

	public void setTotalRent(long totalRent) {
		this.totalRent = totalRent;
	}

}
