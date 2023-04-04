package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {
    public HashMap<Integer,Integer> flightRevenue = new HashMap<>();
    public HashMap<Date,Set<Flight>> dateFlightDb = new HashMap<>();//gives list of flights  for that particular Date.
    public HashMap<Integer,Set<Integer>> passengerFlightDb = new HashMap<>();//database to store how many flights a passenger has booked so far.
    public HashMap<Integer, Set<Integer>> flightPassengerDetailsDb = new HashMap<>();//gives the list of passenger who have booked this flight.
    public HashMap<Integer, Passenger> passengerDb = new HashMap<>();
    public HashMap<City, Airport> cityAirportDb = new HashMap<>();
    public HashMap<Integer,Flight> flightDb = new HashMap<>();

    public String addPassenger(Passenger passenger){
        int key = passenger.getPassengerId();
        passengerDb.put(key,passenger);
        passengerFlightDb.put(passenger.getPassengerId(),new HashSet<>());
        return "SUCCESS";
    }
    public String addAirport(Airport airport){
        City key = airport.getCity();
        cityAirportDb.put(key,airport);
        return "SUCCESS";
    }


    public String getLargestAirportName(){
        String largestAirport  = "";
        int maxTerminal = 0;
        for(Airport airport : cityAirportDb.values()) {
            if (airport.getNoOfTerminals() > maxTerminal) {
                maxTerminal = airport.getNoOfTerminals();
                largestAirport = airport.getAirportName();
            } else if (airport.getNoOfTerminals() == maxTerminal) {
                if (airport.getAirportName().compareTo(largestAirport) < 0)
                    largestAirport = airport.getAirportName();
            }
        }
        return largestAirport;
    }
    public String addFlight(Flight flight){
        //if there is no Airport from where flight would take OFF and where it goes then return null
        if(!cityAirportDb.containsKey(flight.getFromCity()) || (!cityAirportDb.containsKey(flight.getToCity()))){
            return null;
        }
        int flightId = flight.getFlightId();
        flightDb.put(flightId,flight);
        flightPassengerDetailsDb.put(flightId,new HashSet<>());

        //now also update the List of flights for that particular Date
        Date date = flight.getFlightDate();
        Set<Flight> flights = dateFlightDb.get(date);

        if(flights.isEmpty()){
            flights = new HashSet<>();
        }
        flights.add(flight);
        dateFlightDb.put(date,flights);

        //also add this flight into Flight Revenue DB
        flightRevenue.put(flightId,0);
        return "SUCCESS";
    }
    public Flight getFlightById(int flightId){
        return flightDb.get(flightId);
    }
    public Airport getAirportByName(String name){
        for(Airport airport : cityAirportDb.values()){
            if(airport.getAirportName().equals(name))
                return airport;
        }
        return null;
    }
    public String getAirportNameFromFlightId(Integer flightId){
        //first we check if this flight present or not in our DataBase
        if(!flightDb.containsKey(flightId))
            return null;


        Flight flight = getFlightById(flightId);
        City city = flight.getFromCity();

        if(cityAirportDb.containsKey(city))
            return cityAirportDb.get(city).getAirportName();

        return null;
    }
    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity,City toCity){
        double shortestDuration = 1000000000;
        //now we will check in every flight if there is a flight from these city and if yes then check for shortest durstion
        for(Flight flight : flightDb.values()){
            if(flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity)){
                shortestDuration = Math.min(flight.getDuration(),shortestDuration);
            }
        }
        return shortestDuration != 1000000000 ? shortestDuration : -1;
    }
    public String bookATicket(Integer flightId,Integer passengerId){
        //Check if this is a valid flight and passenger or Not
        if((!flightDb.containsKey(flightId)) || (!passengerDb.containsKey(passengerId)))
            return "FAILURE";

        //if flight capacity is full then passenger can not book that flight
        Flight flight = getFlightById(flightId);
        if(flight.getMaxCapacity() < flightPassengerDetailsDb.get(flightId).size())
            return "FAILURE";

        //if passenger has already booked that flight then passenger can not book that flight
        if(flightPassengerDetailsDb.get(flightId).contains(passengerId) && flightPassengerDetailsDb.get(flightId) == null)
            return "FAILURE";
        if(passengerFlightDb.get(passengerId) == null)
            return "FAILURE";

        //else passenger can free to book a ticket
        Set<Integer> passengers = flightPassengerDetailsDb.get(flightId);
        passengers.add(passengerId);
        flightPassengerDetailsDb.put(flightId,passengers);

        //update list of flights for a particular passenger
        Set<Integer> flights = passengerFlightDb.get(passengerId);
        flights.add(flightId);
        passengerFlightDb.put(passengerId,flights);

        //calculate the flight price for the current passenger
        int flightPrice = calculateFlightFare(flightId);
        int revenueSoFar = flightRevenue.get(flightId);
        revenueSoFar += flightPrice;

        //now update the revenue for that Flight
        flightRevenue.put(flightId,revenueSoFar);

        return "SUCCESS";
    }
    public String cancelATicket(Integer flightId,Integer passengerId){
        //Check if this is a valid flight and passenger or Not and
        if((!flightDb.containsKey(flightId)) || (!passengerDb.containsKey(passengerId)) && passengerFlightDb.get(passengerId) == null)
            return "FAILURE";

        //if passenger has booked  ticket but not for this flight
        if(!passengerFlightDb.get(passengerId).contains(flightId) && flightPassengerDetailsDb.get(flightId) == null)
            return "FAILURE";


        // now also decrease the price from flightRevenue DB for that we need at which term this passenger has come for
        // booking
        Set<Integer> flightPassenger = flightPassengerDetailsDb.get(flightId);
        List<Integer> flightList = new ArrayList<Integer>(flightPassenger);
        int passengerTermOfBooking = flightList.indexOf(passengerId);
        int getFlightBookingPrice = 3000 + ((passengerTermOfBooking-1)*50);
        int revenueSoFar = flightRevenue.get(flightId);
        revenueSoFar -= getFlightBookingPrice;
        flightRevenue.put(flightId,revenueSoFar);

        //remove passenger details from flight booking database
        Set<Integer> passengers = flightPassengerDetailsDb.get(flightId);
        passengers.remove(passengerId);
        flightPassengerDetailsDb.put(flightId,passengers);


        //remove flight details from passenger booking database
        Set<Integer> flights = passengerFlightDb.get(passengerId);
        flights.remove(flightId);
        passengerFlightDb.put(passengerId,flights);



        return "SUCCESS";
    }
    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        if(!passengerDb.containsKey(passengerId))
            return 0;

        return passengerFlightDb.get(passengerId).size();
    }
    public int getNumberOfPeopleOn(Date date,String airportName){
        //if on that date there is no flight just return ZERO
        if(!dateFlightDb.containsKey(date) && dateFlightDb.get(date) == null)
            return 0;

        //check if airport with name Exists or not
        Airport airport = getAirportByName(airportName);
        if(airport == null)
            return 0;

        City city = airport.getCity();
        //now get the lists of flight for that particular date
        Set<Flight> flights = dateFlightDb.get(date);
        int totalPassenger = 0;

        //now for each flight on that date check if its boarding or arrival any one of them schedule to that airport or Not
        for(Flight flight : flights){
            if(flight.getFromCity().equals(city) || flight.getToCity().equals(city)){
                totalPassenger += flightPassengerDetailsDb.get(flight.getFlightId()).size();
            }
        }
        return totalPassenger;
    }
    public int calculateFlightFare(int flightId){
        if(!flightDb.containsKey(flightId) && flightPassengerDetailsDb.get(flightId) == null)
            return 0;

        return 3000 + (flightPassengerDetailsDb.get(flightId).size())*50;
    }
    public int calculateRevenueOfAFlight(Integer flightId){
        return flightRevenue.get(flightId);
    }
}
