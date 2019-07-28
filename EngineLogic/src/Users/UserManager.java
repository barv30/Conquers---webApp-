package Users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and for that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<User> usersSet;

    public UserManager() {
        usersSet = new HashSet<>();
    }

    public Set<User> getUsersSet() {
        return usersSet;
    }

    public synchronized void addUser(String i_Username) {

        usersSet.add(new Users.User(i_Username));
    }

    public synchronized void removeUser(String username) {

        usersSet.remove(findUser(username));
    }

    public synchronized Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(findUser(username));
    }

    public synchronized User findUser(String i_UserName){

        for (User currUser:usersSet) {
            if(currUser.getName().equals(i_UserName))
                return currUser;
        }
            return null;
    }
}
