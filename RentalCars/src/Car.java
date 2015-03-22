import java.util.Comparator;
/*
 * Class to represent a car 
 * it dictates the describing details of a car to store and use for comparison later
 */

public class Car {

	private String sipp, name, supplier;
	private double price;
	private double rating;
	private double score;
	
	public Car(String sipp, String name, double price, String supplier, double rating)
	{
		this.sipp = sipp;
		this.name = name;
		this.supplier = supplier;
		this.price = price;
		this.rating = rating;
		this.score = scoreCar();		
	
	}
	
	//applying a score to each car 
	//note that future development should be easier in the regard that only this method needs to be changed
	private double scoreCar(){
		double myScore = 0;
		
		char transmission = this.sipp.charAt(this.sipp.length()-2);
		char fuelAirCon = this.sipp.charAt(this.sipp.length()-1);

		if(transmission=='M')
		{
			myScore +=1;
			
			
		}
		else if(transmission =='A')
		{
			myScore+=5;
			
		}
		
		if(fuelAirCon =='R')
		{
			myScore+=2;
			
		}
		return myScore;
	}
	
	//accessor methods
	public String getSipp()
	{
		return this.sipp;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getSupplier()
	{
		return this.supplier;
	}
	
	public double getPrice()
	{
		return this.price;
	}
	
	public double getRating()
	{
		return this.rating;
	}
	
	public double getScore()
	{
		return  this.score;
	}
	
	@Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj != null && obj instanceof Car)
	    {
	    	Car other = (Car) obj;
	    	if(this.getName().equals(other.getName())
	    			&&this.getSipp().equals(other.getSipp())
	    			&&this.getSupplier().equals(other.getSupplier())
	    			&& (this.getPrice()==other.getPrice())
	    			&& (this.getRating()==other.getRating())
	    			&& (this.getScore()==other.getScore())
	    			)
	    	{
	    		return true;
	    	}
	    	else 
	    	{
	    		return false;
	    	}    
	    }
	      return false;
	  }
	
	@Override
	public String toString()
	{
		return "" + sipp + "-" + name + "-" + price  +"-" + supplier + "-"+ rating;
	}


//static class to compare prices used to order lists of cars
public static class CarPriceCompare implements Comparator<Car> 
{
	private boolean asc;
	public CarPriceCompare(boolean d)
	{
		asc =d;
	}
	
	@Override
	public int compare(Car o1, Car o2) 
	{
		if(asc)
		{
			
			if(o1.getPrice()>o2.getPrice())
				return 1;
			else if (o1.getPrice()<o2.getPrice())
				return -1;
			else
			return 0;
		}
		else
		{
			
			if(o1.getPrice()<o2.getPrice())
				return 1;
			else if (o1.getPrice()>o2.getPrice())
				return -1;
			else
			return 0;
		}
	}
	
}


//static class to compare rating used to order lists of cars
public static class CarRatingCompare implements Comparator<Car>
{

	private boolean asc;
	public CarRatingCompare(boolean d)
	{
		asc =d;
	}
	@Override
	public int compare(Car o1, Car o2) 
	{
		if(asc)
		{
			if(o1.getRating()>o2.getRating())
				return 1;
			else if (o1.getRating()<o2.getRating())
				return -1;
			else
			return 0;
		}
		else
		{
			if(o1.getRating()<o2.getRating())
				return 1;
			else if (o1.getRating()>o2.getRating())
				return -1;
			else
			return 0;
		}
	}
	
}

//static class to compare score used to order lists of cars
public static class CarScoreCompare implements Comparator<Car>
{

	private boolean asc;
	public CarScoreCompare(boolean d)
	{
		asc =d;
	}
	
	@Override
	public int compare(Car o1, Car o2)
	{
		if(asc)
		{
			if((o1.getScore()+o1.getRating())> (o2.getScore()+o2.getRating()))
				return 1;
			else if ((o1.getScore()+o1.getRating())< (o2.getScore()+o2.getRating()))
				return -1;
			else
			return 0;
		}
		else
		{
			if((o1.getScore()+o1.getRating())< (o2.getScore()+o2.getRating()))
				return 1;
			else if ((o1.getScore( )+o1.getRating())> (o2.getScore()+o2.getRating()))
				return -1;
			else
			return 0;
		}
	}
	
}

}//end class Car

