package com.app.diamondhotelbackend.util;

import java.util.Arrays;
import java.util.List;

public class ConstantUtil {

    /**
     * ----- IMAGE -----
     **/
    public static final int MAX_IMAGE_SIZE = 4 * 1024;


    /**
     * ----- GRANTED AUTHORITY -----
     **/
    public static final String USER = "USER";

    public static final String ADMIN = "ADMIN";


    /**
     * ----- JWT -----
     **/
    public static final String LOCAL = "LOCAL";

    public static final String JWT_CLAIM_AUTHORITIES = "authorities";


    /**
     * ----- OAUTH2 -----
     **/
    public static final String OAUTH2 = "OAUTH2";

    public static final String OAUTH2_ATTR_NAME = "name";

    public static final String OAUTH2_ATTR_GIVEN_NAME = "given_name";

    public static final String OAUTH2_ATTR_FAMILY_NAME = "family_name";

    public static final String OAUTH2_ATTR_PICTURE = "picture";

    public static final String OAUTH2_ATTR_EMAIL = "email";

    public static final String OAUTH2_ATTR_CONFIRMED = "confirmed";

    public static final String OAUTH2_ATTR_ID = "id";

    public static final String OAUTH2_ATTR_ACCESS_TOKEN = "access-token";

    public static final String OAUTH2_ATTR_REFRESH_TOKEN = "refresh-token";

    public static final String OAUTH2_ATTR_ERROR = "error";

    public static final String OAUTH2_CALLBACK_URI = "/sign-in/oauth2/callback";


    /**
     * ----- PAYMENT -----
     **/
    public static final String REFUND = "refund";

    public static final String SUCCEEDED = "succeeded";

    public static final String APPROVED = "approved";

    public static final String WAITING_FOR_PAYMENT = "waiting-for-payment";

    public static final String CANCELLED = "cancelled";


    /**
     * ----- EXCEPTION -----
     **/
    public static final String INVALID_AUTH_PROVIDER_EXCEPTION = "Invalid auth provider";

    public static final String PASSWORD_EXISTS_EXCEPTION = "Password exists";

    public static final String EMAIL_EXISTS_EXCEPTION = "Email exists";

    public static final String USER_PROFILE_NOT_FOUND_EXCEPTION = "User profile not found";

    public static final String EMAIL_NOT_FOUND_FROM_OAUTH_2_PROVIDER_EXCEPTION = "Email not found from OAuth2 provider";

    public static final String USER_PROFILE_EXISTS_EXCEPTION = "User profile exists";

    public static final String NOT_ENOUGH_AVAILABLE_ROOMS = "Not enough available rooms";

    public static final String INVALID_TOKEN_SIGNATURE_EXCEPTION = "Invalid JWT signature";

    public static final String INVALID_TOKEN_EXCEPTION = "Invalid JWT token";

    public static final String TOKEN_IS_EXPIRED_EXCEPTION = "JWT token is expired";

    public static final String TOKEN_IS_UNSUPPORTED_EXCEPTION = "JWT token is unsupported";

    public static final String TOKEN_CLAIMS_STRING_IS_EMPTY_EXCEPTION = "JWT claims string is empty";

    public static final String CONFIRMATION_TOKEN_NOT_FOUND_EXCEPTION = "Email token not found";

    public static final String CONFIRMATION_TOKEN_ALREADY_CONFIRMED_EXCEPTION = "Confirmation token already confirmed";

    public static final String CONFIRMATION_TOKEN_ALREADY_EXPIRED_EXCEPTION = "Confirmation token already expired";

    public static final String INVALID_PARAMETERS_EXCEPTION = "Invalid parameters";

    public static final String ROOM_TYPE_NOT_FOUND_EXCEPTION = "Room type not found";

    public static final String AVAILABLE_ROOM_NOT_FOUND_EXCEPTION = "Available room not found";

    public static final String PAYMENT_NOT_FOUND_EXCEPTION = "Payment not found";

    public static final String FLIGHT_NOT_FOUND_EXCEPTION = "Flight not found";

    public static final String RESERVATION_NOT_FOUND_EXCEPTION = "Reservation not found exception";

    public static final String TOO_LATE_TO_CANCEL_RESERVATION = "Too late to cancel reservation";

    public static final String PAYMENT_EXPIRED_EXCEPTION = "Payment expired";

    public static final String CHARGE_NOT_FOUND_EXCEPTION = "Charge not found";

    public static final String RESERVATION_EXPIRED_EXCEPTION = "Reservation expired exception";

    public static final String ROOM_NOT_FOUND = "Room not found";


    /**
     * ----- OPINION -----
     **/
    public static final String BAD = "BAD";

    public static final String GOOD = "GOOD";

    public static final String EXCELLENT = "EXCELLENT";


    /**
     * ----- OPEN WEATHER -----
     **/
    public static final String OPEN_WEATHER_BASE_URI = "https://api.openweathermap.org";

    public static final String OPEN_WEATHER_VALUE_LAT = "4.195762812224563";

    public static final String OPEN_WEATHER_ATTR_LAT = "lat";

    public static final String OPEN_WEATHER_ATTR_LON = "lon";

    public static final String OPEN_WEATHER_ATTR_EXCLUDE = "exclude";

    public static final String OPEN_WEATHER_ATTR_APPID = "appid";

    public static final String OPEN_WEATHER_ATTR_UNITS = "units";

    public static final String OPEN_WEATHER_VALUE_UNITS = "metric";

    public static final String OPEN_WEATHER_VALUE_EXCLUDE = "minutely,hourly,alerts";

    public static final String OPEN_WEATHER_VALUE_LON = "73.52610223044698";


    /**
     * ----- EMAIL -----
     **/
    public static final String EMAIL_SENDER = "diamond.hotel.contact@gmail.com";

    public static final String EMAIL_ATTR_CONFIRMATION_TOKEN = "confirmation-token";

    public static final String EMAIL_CONFIRM_ACCOUNT_SUBJECT = "Diamond hotel - confirm your email";

    public static final String EMAIL_CONFIRM_ACCOUNT_CALLBACK_URI = "/account/confirmation";

    public static final String EMAIL_CHANGE_PASSWORD_CALLBACK_URI = "/forgot/password";

    public static final String EMAIL_CHANGE_PASSWORD_SUBJECT = "Diamond hotel - change your password";

    public static final String EMAIL_RESERVATION_CONFIRMED_SUBJECT = "Diamond hotel - new reservation";

    public static final String EMAIL_PAYMENT_FOR_RESERVATION_CONFIRMED_SUBJECT = "Diamond hotel - reservation paid";

    public static final String EMAIL_PAYMENT_FOR_RESERVATION_CANCELLED_SUBJECT = "Diamond hotel - reservation cancelled";

    /**
     * ----- STATISTICS -----
     **/
    public static final List<String> STATISTICS_MONTH_LIST = Arrays.asList(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    );

    public static final String STATISTICS_NONE = "None";

    public static final String STATISTICS_USERS = "Created users";

    public static final String STATISTICS_RESERVATIONS = "Reservations";

    public static final String STATISTICS_RESERVED_ROOMS = "Reserved rooms";

    public static final String STATISTICS_INCOME = "Income";
}
