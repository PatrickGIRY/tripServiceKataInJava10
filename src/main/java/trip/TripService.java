package trip;

import exception.UserNotLoggedInException;
import user.User;
import user.UserSession;

import java.util.ArrayList;
import java.util.List;

public class TripService {

	public List<Trip> getTripsByUser(User user) throws UserNotLoggedInException {
		List<Trip> tripList = new ArrayList<Trip>();
		User loggedUser = getLoggedUser();
		boolean isFriend = false;
		if (loggedUser != null) {
			for (User friend : user.getFriends()) {
	   			if (friend.equals(loggedUser)) {
					isFriend = true;
					break;
				}
			}
			if (isFriend) {
				tripList = tripsBy(user);
			}
			return tripList;
		} else {
			throw new UserNotLoggedInException();
		}
	}

	List<Trip> tripsBy(User user) {
		return TripDAO.findTripsByUser(user);
	}

	User getLoggedUser() {
		return UserSession.getInstance().getLoggedUser();
	}

}
