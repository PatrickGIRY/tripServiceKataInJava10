package trip;

import exception.UserNotLoggedInException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TripServiceShould {

    private static final Trip TO_ITALY = new Trip();
    private static final Trip TO_LONDON = new Trip();

    private static final User UNUSED_USER = null;
    private static final User GUEST = null;
    private static final User REGISTRED_USER = new User();
    private static final User ANOTHER_USER = new User();

    private User loggedUser;
    private TripService tripService;

    @BeforeEach
    void setUp() {
        tripService = new TestableTripService();
    }

    @Test
    void throw_an_exception_when_user_not_logged_in() {
        loggedUser = GUEST;
        assertThatThrownBy(() -> tripService.getTripsByUser(UNUSED_USER))
                .isInstanceOf(UserNotLoggedInException.class);
    }

    @Test
    void not_return_any_trips_when_users_are_not_friends() {
       loggedUser = REGISTRED_USER;

       User friend = new User();
       friend.addFriend(ANOTHER_USER);
       friend.addTrip(TO_ITALY);

       List<Trip> friendTrips = tripService.getTripsByUser(friend);

       assertThat(friendTrips).isEmpty();
    }

    @Test
    void return_friend_trips_when_users_are_friends() {
        loggedUser = REGISTRED_USER;

        User friend = new User();
        friend.addFriend(loggedUser);
        friend.addTrip(TO_ITALY);
        friend.addTrip(TO_LONDON);

        List<Trip> friendTrips = tripService.getTripsByUser(friend);

        assertThat(friendTrips).contains(TO_ITALY, TO_LONDON);
    }

    private class TestableTripService extends TripService {
        @Override
        User getLoggedUser() {
            return loggedUser;
        }

        @Override
        List<Trip> tripsBy(User user) {
            return user.trips();
        }
    }
}