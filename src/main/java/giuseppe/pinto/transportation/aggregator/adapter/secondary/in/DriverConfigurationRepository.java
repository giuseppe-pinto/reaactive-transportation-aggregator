package giuseppe.pinto.transportation.aggregator.adapter.secondary.in;

import giuseppe.pinto.transportation.aggregator.domain.SearchRequest;
import giuseppe.pinto.transportation.aggregator.port.out.MultiTripReactiveDriverRepository;

import java.util.List;

public interface DriverConfigurationRepository {
    List<MultiTripReactiveDriverRepository> getDriversFor(SearchRequest searchRequest);
}
