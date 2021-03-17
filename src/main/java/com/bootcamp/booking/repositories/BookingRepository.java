package com.bootcamp.booking.repositories;

import com.bootcamp.booking.DTOS.*;
import com.bootcamp.booking.exceptions.BadRequestException;
import com.bootcamp.booking.exceptions.NoDestinationException;
import com.bootcamp.booking.exceptions.NotAvailabilityException;
import com.bootcamp.booking.utils.XLSXToJSONConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Repository
public class BookingRepository implements IBookingRepository {

    private final List<HotelDTO> hotels;
    private List<FlightDTO> flights;
    private List<PersonaDTO> people;

    @SuppressWarnings("all")
    public BookingRepository() {
        //Hago uso de la clase utilitaria que lee el archivo excel y lo transforma en JSON.
        XLSXToJSONConverter.toJSON(XLSXToJSONConverter.getRowsFromXLSX("Hoteles.xlsx"), "Hoteles.json");
        XLSXToJSONConverter.toJSON(XLSXToJSONConverter.getRowsFromXLSX("Vuelos.xlsx"), "Vuelos.json");
        XLSXToJSONConverter.toJSON(XLSXToJSONConverter.getRowsFromXLSX("Personas.xlsx"), "Personas.json");

        hotels = getHotelFromJSON("Hoteles.json");
        flights = getFlightFromJSON("Vuelos.json");
        people = getPeopleFromJSON("Personas.json");
    }

    @Override
    public List<HotelDTO> getHotels() {
        return this.hotels;
    }

    @Override
    public List<HotelDTO> getHotelsByDatesAndDestination(HotelParamsDTO params) throws NotAvailabilityException, NoDestinationException {
        List<HotelDTO> result = new ArrayList<>(this.hotels);
        result.removeIf(h -> !h.getDestination().equals(params.getDestination()));
        if (!result.isEmpty()) {
            result.removeIf(hotel -> !availible(hotel, params.getDateFrom(), params.getDateTo()));
            if (!result.isEmpty()) {
                return result;
            } else {
                throw new NotAvailabilityException();
            }
        } else {
            throw new NoDestinationException();
        }
    }

    @Override
    public HotelDTO getHotelByCode(String hotelCode) throws BadRequestException {
        for (HotelDTO h : hotels) {
            if (h.getHotelCode().equals(hotelCode))
                return h;
        }
        throw new BadRequestException("El código de hotel es incorrecto.");
    }

    private boolean availible(HotelDTO hotel, Date from, Date to) {
        for (AvailableDTO dates : hotel.getAvailableDates()) {
            if ((dates.getDateFrom().before(from) | dates.getDateFrom().equals(from))
                    & (dates.getDateTo().after(to) | dates.getDateTo().equals(to))) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static List<HotelDTO> getHotelFromJSON(String path) {
        List<HotelDTO> hoteles = new ArrayList<>();
        FileReader reader;
        File file;
        try {
            file = ResourceUtils.getFile("classpath:" + path);
            reader = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (JSONObject jo : (Iterable<JSONObject>) jsonArray) {
                HotelDTO h = new HotelDTO();
                h.setHotelCode((String) jo.get("Código Hotel"));
                h.setName((String) jo.get("Nombre"));
                h.setDestination((String) jo.get("Lugar/Ciudad"));
                h.setRoomType(getTypeOfRoom((String) jo.get("Tipo de Habitación")));
                h.setPrice(Double.valueOf(cleanPrice((String) jo.get("Precio por noche"))));

                DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                List<AvailableDTO> dates = new ArrayList<>();
                AvailableDTO available = new AvailableDTO();
                available.setDateFrom(dateFormat.parse((String) jo.get("Disponible Desde")));
                available.setDateTo(dateFormat.parse((String) jo.get("Disponible hasta")));
                dates.add(available);
                h.setAvailableDates(dates);
                h.setAvailable(jo.get("Reservado").equals("NO"));

                hoteles.add(h);
            }
            reader.close();
            return hoteles;

        } catch (Exception e) {
            e.printStackTrace();
            return hoteles;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<FlightDTO> getFlightFromJSON(String path) {
        List<FlightDTO> flights = new ArrayList<>();
        FileReader reader;
        File file;
        try {
            file = ResourceUtils.getFile("classpath:" + path);
            reader = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (JSONObject jo : (Iterable<JSONObject>) jsonArray) {
                FlightDTO f = new FlightDTO();
                f.setFlightNumber((String) jo.get("Nro Vuelo"));
                f.setOrigin((String) jo.get("Origen"));
                f.setDestination((String) jo.get("Destino"));
                f.setSeatType((String) jo.get("Tipo Asiento"));
                f.setPrice(Double.valueOf(cleanPrice((String) jo.get("Precio por persona"))));

                DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                f.setDateFrom(dateFormat.parse((String) jo.get("Fecha ida")));
                f.setDateTo(dateFormat.parse((String) jo.get("Fecha Vuelta")));

                flights.add(f);
            }
            reader.close();
            return flights;

        } catch (Exception e) {
            e.printStackTrace();
            return flights;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<PersonaDTO> getPeopleFromJSON(String path) {
        List<PersonaDTO> people = new ArrayList<>();
        FileReader reader;
        File file;
        try {
            file = ResourceUtils.getFile("classpath:" + path);
            reader = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (JSONObject jo : (Iterable<JSONObject>) jsonArray) {
                PersonaDTO p = new PersonaDTO();
                p.setDni(getDni((String) jo.get("DNI")));
                p.setName((String) jo.get("Nombre"));
                p.setLastname((String) jo.get("Apellido"));

                DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                p.setBirthDate(dateFormat.parse((String) jo.get("Fecha Nacimiento")));

                p.setMail((String) jo.get("E-mail"));

                people.add(p);
            }
            reader.close();
            return people;

        } catch (Exception e) {
            e.printStackTrace();
            return people;
        }
    }

    private static String cleanPrice(String price) {
        char[] arr = price.toCharArray();
        StringBuilder result = new StringBuilder();

        for (char c : arr) {
            if (c != '$' & c != '.') {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static Integer getDni(String dni) {
        StringBuilder result = new StringBuilder();
        int cont = 0;
        char[] arr = dni.toCharArray();
        for (char c : arr) {
            if (cont < 8) {
                if (c == '0' | c == '1' | c == '2' | c == '3' | c == '4'
                        | c == '5' | c == '6' | c == '7' | c == '8' | c == '9') {
                    result.append(c);
                    cont++;
                }
            } else {
                break;
            }
        }

        return Integer.parseInt(result.toString());
    }

    private static String getTypeOfRoom(String type) {
        switch (type) {
            case "Single":
                return "SINGLE";
            case "Doble":
                return "DOUBLE";
            case "Triple":
                return "TRIPLE";
            default:
                return "MULTIPLE";
        }
    }
}
