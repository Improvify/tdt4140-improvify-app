package tdt4140.gr1817.app.core.feature.user.vitaldata;

import lombok.Value;
import tdt4140.gr1817.app.core.feature.user.HonorUserPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProvider;
import tdt4140.gr1817.ecosystem.persistence.data.ServiceProviderPermissions;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.ServiceProviderPermissionsRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetRestingHeartRateByUserSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification
        .GetServiceProviderPermissionByIdSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetWeightByUserSpecification;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class GetVitalDataForUser {

    private WeightRepository weightRepository;
    private RestingHeartRateRepository restingHeartRateRepository;
    private final HonorUserPermissions honorUserPermissions;
    private final ServiceProvider serviceProvider;
    private final ServiceProviderPermissionsRepository serviceProviderPermissionsRepository;

    private User user;

    @Inject
    public GetVitalDataForUser(WeightRepository weightRepository,
                               RestingHeartRateRepository restingHeartRateRepository,
                               HonorUserPermissions honorUserPermissions,
                               @Named("improvify") ServiceProvider serviceProvider,
                               ServiceProviderPermissionsRepository serviceProviderPermissionsRepository) {
        this.weightRepository = weightRepository;
        this.restingHeartRateRepository = restingHeartRateRepository;
        this.honorUserPermissions = honorUserPermissions;
        this.serviceProvider = serviceProvider;
        this.serviceProviderPermissionsRepository = serviceProviderPermissionsRepository;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VitalData load() {
        if (user == null || serviceProvider == null) {
            throw new IllegalStateException("User and service provider must be set! User: " + user
                    + ". Service provider: " + serviceProvider);
        }

        int userId = user.getId();
        int serviceProviderId = serviceProvider.getId();
        ServiceProviderPermissions permissions = serviceProviderPermissionsRepository.query(new
                GetServiceProviderPermissionByIdSpecification(userId, serviceProviderId))
                .get(0);

        return new VitalData(user, retrieveWeights(user, permissions), retrieveHeartRates(user, permissions));
    }

    private List<Weight> retrieveWeights(User user, ServiceProviderPermissions permissions) {
        List<Weight> weights = weightRepository.query(new GetWeightByUserSpecification(user));
        weights = honorUserPermissions.honorUsersWeightPermissions(permissions, weights);
        return weights;
    }

    private List<RestingHeartRate> retrieveHeartRates(User user, ServiceProviderPermissions permissions) {
        List<RestingHeartRate> heartRates
                = restingHeartRateRepository.query(new GetRestingHeartRateByUserSpecification(user));
        heartRates = honorUserPermissions.honorUsersRestingHeartRatePermissions(permissions, heartRates);
        return heartRates;
    }


    @Value
    public static class VitalData {
        User user;
        List<Weight> weights;
        List<RestingHeartRate> heartRates;
    }
}
