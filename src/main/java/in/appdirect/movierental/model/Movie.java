package in.appdirect.movierental.model;

public class Movie {
	
	private String id;

	private String name;
	
	private String actor;
	
	private String date;
	
	private Long rent;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getRent() {
		return rent;
	}

	public void setRent(Long rent) {
		this.rent = rent;
	}


	public Movie(String id, String name, String actor, String date, Long rent) {
		super();
		this.id = id;
		this.name = name;
		this.actor = actor;
		this.date = date;
		this.rent = rent;
	}

	@Override
	public String toString() {
		return "Movie [name=" + name + ", actor=" + actor + ", date=" + date
				+ ", rent=" + rent + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
