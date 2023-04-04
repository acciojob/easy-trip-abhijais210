package com.driver.service;

import com.driver.model.City;
import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AirportService {
    AirportRepository airportRepository = new AirportRepository();
    public String addPassenger(Passenger passenger){
        return airportRepository.addPassenger(passenger);
    }
    public String addAirport(Airport airport){
        return airportRepository.addAirport(airport);
    }
    public String getLargestAirportName(){
        return airportRepository.getLargestAirportName();
    }
    public String addFlight(Flight flight){
        return airportRepository.addFlight(flight);
    }
    public String getAirportNameFromFlightId(Integer flightId){
        return airportRepository.getAirportNameFromFlightId(flightId);
    }
    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity,City toCity){
        return airportRepository.getShortestDurationOfPossibleBetweenTwoCities(fromCity,toCity);
    }
    public String bookATicket(Integer flightId,Integer passengerId){
        return airportRepository.bookATicket(flightId,passengerId);
    }
    public String cancelATicket(Integer flightId,Integer passengerId){
        return airportRepository.cancelATicket(flightId,passengerId);
    }
    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }
    public int getNumberOfPeopleOn(Date date, String airportName){
        return airportRepository.getNumberOfPeopleOn(date,airportName);
    }
    public int calculateFlightFare(int flightId){
        return airportRepository.calculateFlightFare(flightId);
    }
    public int calculateRevenueOfAFlight(Integer flightId){
        return calculateRevenueOfAFlight(flightId);
    }
}
