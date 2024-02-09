package giuseppe.pinto.transportation.aggregator.adapter.secondary.in;

import giuseppe.pinto.transportation.aggregator.domain.Driver;
import giuseppe.pinto.transportation.aggregator.domain.DriverOutcome;
import giuseppe.pinto.transportation.aggregator.domain.OneWaySearchRequest;
import giuseppe.pinto.transportation.aggregator.domain.Trip;
import giuseppe.pinto.transportation.aggregator.port.out.DriverConfigurationRepository;
import giuseppe.pinto.transportation.aggregator.port.out.DriverRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static giuseppe.pinto.transportation.aggregator.domain.Driver.BLUE;
import static giuseppe.pinto.transportation.aggregator.domain.Driver.RED;

class StandardDriverOutcomeServiceTest {


    public static final String DEPARTURE = "MIL";
    public static final String ARRIVAL = "NAP";

    @Test
    void allTheDriversAreCalledAndReturnTripsInDifferentMoment() {

        StandardDriverOutcomeService underTest =
                new StandardDriverOutcomeService(new FakeDriverConfigurationRepository());

        Flux<DriverOutcome> actualTrips = underTest.from(createSearchRequest());

        StepVerifier.create(actualTrips)
                .expectNext(
                        new DriverOutcome(List.of(createTripWith(createSearchRequest(), BLUE))),
                        new DriverOutcome(List.of(createTripWith(createSearchRequest(), RED))))
                .expectComplete()
                .verifyThenAssertThat()
                .tookMoreThan(Duration.ofSeconds(3));

    }

    private static class FakeDriverConfigurationRepository implements DriverConfigurationRepository{


        @Override
        public List<DriverRepository> getDriversFor(OneWaySearchRequest oneWaySearchRequest) {

            DriverRepository firstBlockingDriverRepository = searchRequest -> Mono.just(new DriverOutcome
                            (List.of(createTripWith(searchRequest, BLUE))))
                    .delayElement(Duration.ofSeconds(1));

            DriverRepository secondBlockingDriverRepository = searchRequest -> Mono.just(new DriverOutcome((List.of(createTripWith(searchRequest, RED)))))
                    .delayElement(Duration.ofSeconds(3));

            return List.of(firstBlockingDriverRepository, secondBlockingDriverRepository);
        }


    }

    private OneWaySearchRequest createSearchRequest() {

        return new OneWaySearchRequest(DEPARTURE,
                ARRIVAL,
                LocalDate.now()
        );

    }


    private static Trip createTripWith(OneWaySearchRequest oneWaySearchRequest, Driver driver) {
        return new Trip(
                oneWaySearchRequest.departure(),
                oneWaySearchRequest.arrival(),
                LocalDateTime.of(2023, Month.NOVEMBER, 12, 10, 0),
                LocalDateTime.of(2023, Month.NOVEMBER, 13, 10, 0),
                "1000",
                "AIRLINE",
                new BigDecimal("10.00"),
                Currency.getInstance(Locale.ITALY),
                driver);
    }
}