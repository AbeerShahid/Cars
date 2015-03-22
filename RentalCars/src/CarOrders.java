import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
 





import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/* A class to order cars based on different criteria.
 * This provides the console interface.
 * @ Abeer Shahid 22/03/2015
 */
public class CarOrders {
	
	//the json file with the vehicles
	//although not done here it may be possible
	//to provide this as an argument to the program in later versions
	private static final String path ="src/InputFiles/vehicles.json";
	
	//paths for the files containing car sipp details
	private static final String carM ="src/InputFiles/CarMapping.txt";
	private static final String carDM ="src/InputFiles/CarDoorMapping.txt";
	private static final String carTran= "src/InputFiles/CarTransmissionMapping.txt";
	private static final String carFuelAirC="src/InputFiles/CarFuelAirConMapping.txt";
	
	//method to read a file to a HashMap given a path to file
	private static HashMap<String,String> readToMap(String mapPath)
	{
		HashMap<String, String> map =new HashMap<String, String>();
		try{
			for(String line : Files.readAllLines(Paths.get(mapPath)))
				map.put(line.split(":")[0], line.split(":")[1]);
		}
		//read each line of file till end and add it to the HashMap
		catch(IOException e1){
			System.err.println("Error reading file. " +e1.getMessage());
		}
		catch(Exception e2)
		{
			System.err.println("Error: " +e2.getMessage());
			
		}
		
		//return Map
		return map;
	}
	
	
	
	//method for getting and printing  all the vehicles in  price order 
	private static void priceOrder(ArrayList<Car> list, boolean asc)
	{
		Collections.sort(list, new Car.CarPriceCompare(asc));
		for(Car car:list)
		{
			System.out.println(car.getName() + " - " + car.getPrice());
		}
	}
	
	//method for retrieving and printing the meaning of the sipp code
	private static void Sipp(ArrayList<Car> list, HashMap<String, String> carType, HashMap<String, String> carDoorType, HashMap<String, String> transmission, HashMap<String,String> fuelAirCon)
	{
		
		for(Car car:list)
		{
			
			System.out.print(car.getName() + " - " + car.getSipp() +" - " );
			String sipp = car.getSipp();
			
			String carT = carType.get( String.valueOf(sipp.charAt(0)));
			String carD = carDoorType.get( String.valueOf(sipp.charAt(1)));
			String trans = transmission.get( String.valueOf(sipp.charAt(2)));
			String fuelA = fuelAirCon.get( String.valueOf(sipp.charAt(3)));
			System.out.println(carT+ " - " + carD + " - " + trans + " - " + fuelA);
			
		}
	}
	
	//method for the retrieving and printing the highest rated supplier per car type
	private static void highestRatedSupplier(ArrayList<Car> list, HashMap<String, String> carType) 
	{
		ArrayList<Car> selected=new ArrayList<Car>();
		HashMap<String, ArrayList<Car>> mapCarTypes = new HashMap<String, ArrayList<Car>>();
		
		//create mapping of carType and all vehicles of that carType
		for(int i = 0; i <list.size();i++)
		{
			Car c = list.get(i);
			String sipp = c.getSipp();
			String carT = String.valueOf(sipp.charAt(0));
			
			//if the carType exists add to the list
			if(mapCarTypes.containsKey(carT))
			{
				mapCarTypes.get(carT).add(c);
			}
			
			//if not create a new list and add the carType and the associated list to the mapping
			else
			{
				ArrayList<Car> temp = new ArrayList<Car>();
				temp.add(c);
				mapCarTypes.put(carT,temp);
			}			
		}
		
		
		for(String key : mapCarTypes.keySet())
		{
			//for every key i.e. car type pull out the list of of all the cars of that car type
			ArrayList<Car> mapCarT = mapCarTypes.get(key);
			
			//sort descending according to the rating
			//so the first value in the list will be the best rating
			Collections.sort(mapCarT,new Car.CarRatingCompare(false));
			
			//add the best rating to the selected items to bring forward			
			selected.add(mapCarT.get(0));
			
			//check to see if others of this rating also exist in the list ...
			for(int j=0;j<mapCarT.size();j++)
			{
				if(j!=0)
				{
					Car mC = mapCarT.get(j);
					if(mC.getRating()==mapCarT.get(0).getRating())
					{
						selected.add(mC);
					}
				}
				
			}
		}
		
		//sort descending ...big ->small
		Collections.sort(selected, new Car.CarRatingCompare(false));
		//print results to output
		for(Car c: selected)
		{
			String sipp = c.getSipp();
			String carT = String.valueOf(sipp.charAt(0));
			System.out.println(c.getName()+" - "+ carType.get(carT)+" - " + c.getSupplier()+" - " + c.getRating());
		}			
	}
    
	//order the scores depending and print the result
	public static void scoreOrder(ArrayList<Car> list, boolean asc)
	{
		Collections.sort(list, new Car.CarScoreCompare(asc));
		for(Car c: list)
		{
			System.out.println(c.getName() + " - " + (c.getScore()) + " - "+ c.getRating() +" - " + (c.getScore()+c.getRating()));
		}
	}
		
		
	//	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		
		//car sipp detail maps are constructed 
		HashMap<String, String> carType = readToMap(carM);
		HashMap<String, String> carDoorType = readToMap(carDM);;
		HashMap<String, String> transmission = readToMap(carTran);;
		HashMap<String, String> fuelAirCon = readToMap(carFuelAirC);
		
		//an arraylist of the cars		
		ArrayList<Car> cars = new ArrayList<Car>();
		
		//initialise a parser to parse the json file
		JSONParser parser = new JSONParser();
		
		try{
			//parse the json file into a json object
			Object obj = parser.parse(new FileReader(path));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject search = (JSONObject) jsonObject.get("Search");
			//extract the vehicle listing
			JSONArray vehicles = (JSONArray)search.get("VehicleList");
		
			//for each of the vehicle create a car object
			//to allow easy manipulation and ordering
           for(int i = 0; i < vehicles.size();i++) {
        	   
        	   //access one of the records about vehicles
        	   JSONObject rec = (JSONObject)vehicles.get(i);
        	   
        	   //each of the elemets of the json object representing a vehicle
        	   String sipp = (String) rec.get("sipp");
        	   String name =(String)rec.get("name");
        	   String supplier = (String)rec.get("supplier");
               
            	Object price = null;
            	double p;
            	if(rec.get("price") instanceof Double)
            	{
            		 price =(Double)rec.get("price");
            		 Double d = (Double)price;
             		 p = d.doubleValue();
            	}
            	else if(rec.get("price") instanceof Long)
            	{
            		  price =(Long)rec.get("price");
            		  Long l = (Long) price;
              		  p = l.doubleValue();
            	}
            	else if(rec.get("price") instanceof Integer)
            	{
          		  price =(Integer)rec.get("price");
          		  Integer integer = (Integer)price;
        		  p= integer.doubleValue();
            	}
            	else
            	{
            		p=(double)0;
            	}
            	  	              
            	
                Object rating = null;
                double r;                
            	if(rec.get("rating") instanceof Double)
            	{
            		 rating =(Double)rec.get("rating");
            		 Double d = (Double)rating;
             		 r = d.doubleValue();
            	}
            	else if(rec.get("rating") instanceof Long)
            	{
            		  rating =(Long)rec.get("rating");
            		  Long l = (Long) rating;
              		  r = l.doubleValue();   
            	}
            	else if(rec.get("rating") instanceof Integer)
            	{
            		rating =(Integer)rec.get("rating");
            		Integer integer = (Integer)rating;
            		r= integer.doubleValue();
            	}
            	else
            	{
            		r=(double)0;
            	}
            	
     
            	//create a car object and add to the list of cars for later use 
                Car temp = new Car(sipp, name, p, supplier, r);  
                cars.add(temp);               
            }//for
 
           //start the console input to take input from user and then out put accordingly
           String s; 
           do{
        	   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        	   System.out.print("Enter Choice:");
      	       s = br.readLine();
      	       switch(s)
      	       {
      	       case "A":priceOrder(cars, true);
      	       break;
      	       case "B":Sipp(cars, carType, carDoorType, transmission, fuelAirCon);
      	       break;
      	       case "C":highestRatedSupplier(cars, carType);
      	       break;
      	       case "D": scoreOrder(cars,false);
      	       break;
      	       default:;
      	       break;  	            	       
      	       }
           }while(!s.equals("E")); 	           
  	     
		}//try
		catch(IOException e){
			System.err.println("IO");
		}
		catch(NumberFormatException e1)
		{
			System.err.println("Number Format");
		}
		catch(Exception e2)
		{
			System.err.println(e2.getMessage());
		} 
		
	}//main

}//Car Order Class
