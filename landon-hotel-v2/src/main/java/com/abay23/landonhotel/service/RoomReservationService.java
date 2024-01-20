package com.abay23.landonhotel.service;

import com.abay23.landonhotel.data.entity.Guest;
import com.abay23.landonhotel.data.entity.Reservation;
import com.abay23.landonhotel.data.entity.Room;
import com.abay23.landonhotel.data.repository.GuestRepository;
import com.abay23.landonhotel.data.repository.ReservationRepository;
import com.abay23.landonhotel.data.repository.RoomRepository;
import com.abay23.landonhotel.service.model.RoomReservation;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RoomReservationService {
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomReservationService(GuestRepository guestRepository, RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<RoomReservation> getRoomReservationForDate(String reservationDate){
        Date date  = null;
        if(StringUtils.isNotEmpty(reservationDate)){
            date = Date.valueOf(reservationDate);
        } else {
            date = new Date(new java.util.Date().getTime());
        }
        Map<Long, RoomReservation> roomReservations = new HashMap<>();
        List<Room> rooms = this.roomRepository.findAll();
        rooms.forEach(room -> {
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoomId(room.getId());
            roomReservation.setRoomName(room.getName());
            roomReservation.setRoomNumber(room.getRoomNumber());
            roomReservations.put(roomReservation.getRoomId(), roomReservation);
        });
        List<Reservation> reservations = this.reservationRepository.findAllByReservationDate(date);
        reservations.forEach(reservation ->{
            RoomReservation roomReservation = roomReservations.get(reservation.getRoomId());
            roomReservation.setReservationId(reservation.getId());
            roomReservation.setReservationDate(reservation.getReservationDate().toString());

            Optional<Guest> guest = this.guestRepository.findById(reservation.getGuestId());
            roomReservation.setGuessId(guest.get().getId());
            roomReservation.setFirstName(guest.get().getFirstName());
            roomReservation.setLastName(guest.get().getLastName());
        });
        return roomReservations.values().stream().toList();
    }
}
