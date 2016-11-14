package in.appdirect.movierental.builder;

import in.appdirect.movierental.model.Movie;

public class MovieBuilder {
	
	private String id;

	private String name;
	
	private String actor;
	
	private String date;
	
	private Long rent;

	public Movie build()
    {
		Movie movieVo = new Movie(id, name, actor, date, rent);
		movieVo.setName(name);
		movieVo.setActor(actor);
		movieVo.setDate(date);
		movieVo.setRent(rent);
        return movieVo;
    }
	
	public MovieBuilder withId(String id)
    {
        this.id = id;
        return this;
    }
 
    public MovieBuilder withName(String name)
    {
        this.name = name;
        return this;
    }
    
    public MovieBuilder withRent(Long rent)
    {
        this.rent = rent;
        return this;
    }
    
    public MovieBuilder withActor(String actor)
    {
        this.actor = actor;
        return this;
    }
    
    public MovieBuilder withDate(String date)
    {
        this.date = date;
        return this;
    }
    
}
