package managers;

import users.User;

public interface EditUserInfos {
    void updateUsername(User user);
    void updatePassword(User user);
    void updateAddress(User user);
}
