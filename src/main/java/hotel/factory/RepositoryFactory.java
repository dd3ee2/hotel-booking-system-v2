package hotel.factory;

import hotel.repository.*;

public class RepositoryFactory {

    private RepositoryFactory() {}

    public static IRoomRepository createRoomRepository() {
        return new RoomRepository();
    }

    public static ICustomerRepository createCustomerRepository() {
        return new CustomerRepository();
    }

    public static IBookingRepository createBookingRepository() {
        return new BookingRepository();
    }

    public static IUserRepository createUserRepository() {
        return new UserRepository();
    }
}