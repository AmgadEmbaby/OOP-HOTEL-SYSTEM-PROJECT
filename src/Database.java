/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.*;

public class Database {

  private static  ArrayList<Guests> guestList= new ArrayList<>();

  private static  ArrayList<Rooms> roomList= new ArrayList<>();

  private static  ArrayList<Reservations> reservationsList= new ArrayList<>();

  private static  ArrayList<Amenity> availableAmenitiesList = new ArrayList<>();

  private static  ArrayList<RoomType> availableRoomTypesList = new ArrayList<>();



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

    return availableAmenitiesList;
  }

  public static ArrayList<RoomType> getAvailableRoomTypesList(){

    return availableRoomTypesList;
  }



  static {
    // Arguments: roomfloor, roomtype, isAvailable
    roomList.add(new Rooms(1, "SINGLE"));
    roomList.add(new Rooms(1, "SINGLE"));
    roomList.add(new Rooms(2, "DOUBLE"));
    roomList.add(new Rooms(2, "DOUBLE")); // One occupied room
    roomList.add(new Rooms(3, "SUITE"));
  }

  static {
    // Arguments: name
    availableRoomTypesList.add(new RoomType("single"));
    availableRoomTypesList.add(new RoomType("double"));
    availableRoomTypesList.add(new RoomType("suite"));

  }



}
