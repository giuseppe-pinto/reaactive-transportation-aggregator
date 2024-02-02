package giuseppe.pinto.transportation.aggregator.port.in;

import giuseppe.pinto.transportation.aggregator.domain.DriverOutcome;
import giuseppe.pinto.transportation.aggregator.domain.OneWaySearchRequest;
import reactor.core.publisher.Flux;

public interface DriverOutcomeRepository {

    Flux<DriverOutcome> from(OneWaySearchRequest oneWaySearchRequest);

}

