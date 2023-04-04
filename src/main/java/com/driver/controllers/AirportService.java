package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AirportService {
    AirportRepository airportRepository = new AirportRepository();
    public String addPassenger(Passenger passenger)throws NullPointerException{
        return airportRepository.addPassenger(passenger);
    }
    public String addAirport(Airport airport)throws NullPointerException{
        return airportRepository.addAirport(airport);
    }
    public String getLargestAirportName()throws NullPointerException{
        return airportRepository.getLargestAirportName();
    }
    public String addFlight(Flight flight)throws NullPointerException{
        return airportRepository.addFlight(flight);
    }
    public String getAirportNameFromFlightId(Integer flightId)throws NullPointerException{
        return airportRepository.getAirportNameFromFlightId(flightId);
    }
    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity)throws NullPointerException{
        return airportRepository.getShortestDurationOfPossibleBetweenTwoCities(fromCity,toCity);
    }
    public String bookATicket(Integer flightId,Integer passengerId)throws NullPointerException{
        return airportRepository.bookATicket(flightId,passengerId);
    }
    public String cancelATicket(Integer flightId,Integer passengerId)throws NullPointerException{
        return airportRepository.cancelATicket(flightId,passengerId);
    }
    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId)throws NullPointerException{
        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }
    public int getNumberOfPeopleOn(Date date, String airportName)throws NullPointerException{
        return airportRepository.getNumberOfPeopleOn(date,airportName);
    }
    public int calculateFlightFare(int flightId)throws NullPointerException{
        return airportRepository.calculateFlightFare(flightId);
    }
    public int calculateRevenueOfAFlight(Integer flightId) throws NullPointerException{
        return airportRepository.calculateRevenueOfAFlight(flightId);
    }
}
