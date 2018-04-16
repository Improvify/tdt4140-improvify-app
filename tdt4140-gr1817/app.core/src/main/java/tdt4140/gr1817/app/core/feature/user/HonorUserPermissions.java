package tdt4140.gr1817.app.core.feature.user;

import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HonorUserPermissions {


    public User honorUserPermissions(User dishonoredUser, ServiceProviderPermissions spp) {
        String newFirstname = dishonoredUser.getFirstName();
        String newLastname = dishonoredUser.getLastName();
        Date newBirthday = dishonoredUser.getBirthDate();
        float newHeight = dishonoredUser.getHeight();
        String newUsername = dishonoredUser.getUsername();
        String newEmail = dishonoredUser.getEmail();
        if (!spp.isBirthDate()) {
            newBirthday = null;
        }
        if (!spp.isHeight()) {
            newHeight = User.NO_PERMISSION;
        }
        if (!spp.isUsername()) {
            newUsername = null;
        }
        if (!spp.isName()) {
            newFirstname = null;
            newLastname = null;
        }
        if (!spp.isEmail()) {
            newEmail = null;
        }

        return new User(dishonoredUser.getId(), newFirstname, newLastname, newHeight, newBirthday, newUsername,
                dishonoredUser.getPassword(), newEmail);

    }

    public List<Weight> honorUsersWeightPermissions(ServiceProviderPermissions serviceProviderPermissions,
                                                    List<Weight> weightList) {
        if (!serviceProviderPermissions.isWeight()) {
            return Collections.emptyList();
        }
        return weightList;
    }

    public List<RestingHeartRate> honorUsersRestingHeartRatePermissions(ServiceProviderPermissions
                                                                                serviceProviderPermissions,
                                                                        List<RestingHeartRate> restingHeartRateList) {
        if (!serviceProviderPermissions.isRestingHeartRate()) {
            return Collections.emptyList();
        }
        return restingHeartRateList;
    }
}
