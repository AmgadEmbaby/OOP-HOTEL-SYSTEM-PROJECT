/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.*;

public class Database {
  private static  ArrayList<Guests> guestList= new ArrayList<>();

  private static  ArrayList<Rooms> roomList= new ArrayList<>();




  public static ArrayList<Guests> getGuestList(){
    return guestList;
  }

  public static ArrayList<Rooms> getRoomList(){
    return roomList;
  }
}
