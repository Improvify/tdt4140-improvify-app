package tdt4140.gr1817.app.core.feature.user.vitaldata;

import lombok.Value;
import tdt4140.gr1817.ecosystem.persistence.data.RestingHeartRate;
import tdt4140.gr1817.ecosystem.persistence.data.User;
import tdt4140.gr1817.ecosystem.persistence.data.Weight;
import tdt4140.gr1817.ecosystem.persistence.repositories.RestingHeartRateRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.WeightRepository;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetRestingHeartRateByUserSpecification;
import tdt4140.gr1817.ecosystem.persistence.repositories.mysql.specification.GetWeightByUserSpecification;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Kristian Rekstad.
 *
 * @author Kristian Rekstad
 */
public class GetVitalDataForUser {

    private WeightRepository weightRepository;
    private RestingHeartRateRepository restingHeartRateRepository;

    private User user;

    @Inject
    public GetVitalDataForUser(WeightRepository weightRepository,
                               RestingHeartRateRepository restingHeartRateRepository) {
        this.weightRepository = weightRepository;
        this.restingHeartRateRepository = restingHeartRateRepository;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VitalData load() {
        return new VitalData(user, retrieveWeights(user), retrieveHeartRates(user));
    }

    private List<Weight> retrieveWeights(User user) {
        List<Weight> weights = weightRepository.query(new GetWeightByUserSpecification(user));
        return weights;
    }

    private List<RestingHeartRate> retrieveHeartRates(User user) {
        List<RestingHeartRate> heartRates
                = restingHeartRateRepository.query(new GetRestingHeartRateByUserSpecification(user));
        return heartRates;
    }


    @Value
    public static class VitalData {
        User user;
        List<Weight> weights;
        List<RestingHeartRate> heartRates;
    }
}
