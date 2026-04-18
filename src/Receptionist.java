import java.time.*;
public class Receptionist extends Staff{


  public void checkIn(int reservationID){

   Reservations reservation= Database.findReservation(reservationID);
   LocalDate today= LocalDate.now();
   if(reservation != null) {

       if (today.isBefore(reservation.getCheckin())) {
           System.out.println("Guest is early for their check in date");
       } else if (today.isAfter(reservation.getCheckin())) {
           System.out.println("Guest is late for their check in date");
       } else {
           reservation.room= Database.findRoomByRoomTypeName(reservation.getRoom().getRoomtype().getTypeName());
           reservation.getRoom().setStatus(Rooms.RoomStatus.OCCUPIED);


       }


   }
  }
}
