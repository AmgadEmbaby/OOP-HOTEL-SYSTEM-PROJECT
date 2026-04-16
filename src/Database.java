/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.*;

public class Database {

  private static  ArrayList<Guests> guestList= new ArrayList<>();

  private static  ArrayList<Rooms> roomList= new ArrayList<>();

  private static  ArrayList<Reservations> reservationsList= new ArrayList<>();

  private static  ArrayList<Amenity> amenitiesList= new ArrayList<>();



  public static ArrayList<Guests> getGuestList(){

    return guestList;
  }

  public static ArrayList<Rooms> getRoomList(){
    return roomList;
  }


  public static ArrayList<Reservations> getReservationsList(){

    return reservationsList;
  }


  public static ArrayList<Amenity> getamenitiesList(){

    return amenitiesList;
  }
  static {
    // Arguments: roomfloor, roomtype, isAvailable
    roomList.add(new Rooms(1, "SINGLE", true));
    roomList.add(new Rooms(1, "SINGLE", true));
    roomList.add(new Rooms(2, "DOUBLE", true));
    roomList.add(new Rooms(2, "DOUBLE", false)); // One occupied room
    roomList.add(new Rooms(3, "SUITE", true));
  }




}
