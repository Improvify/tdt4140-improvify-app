package tdt4140.gr1817.serviceprovider.webserver.validation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;

import javax.inject.Inject;

public class ServiceProviderPermissionsValidator implements Validator {

    private final Gson gson;

    @Inject
    public ServiceProviderPermissionsValidator(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean validate(String json) {
        try {
            ServiceProviderPermissions permissions = gson.fromJson(json, ServiceProviderPermissions.class);
            return true;

        } catch (JsonSyntaxException | NullPointerException | NumberFormatException e) {
            return false;
        }
    }
}
