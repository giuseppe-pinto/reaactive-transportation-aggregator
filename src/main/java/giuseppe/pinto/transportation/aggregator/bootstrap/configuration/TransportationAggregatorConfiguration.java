package giuseppe.pinto.transportation.aggregator.bootstrap.configuration;

import giuseppe.pinto.transportation.aggregator.adapter.primary.rest.controller.TransportationAggregatorController;
import giuseppe.pinto.transportation.aggregator.adapter.primary.rest.dto.RequestAdapter;
import giuseppe.pinto.transportation.aggregator.adapter.primary.rest.dto.SolutionsAdapter;
import giuseppe.pinto.transportation.aggregator.adapter.secondary.in.StandardSearchUseCase;
import giuseppe.pinto.transportation.aggregator.adapter.secondary.in.StandardSuppliersOutcomeService;
import giuseppe.pinto.transportation.aggregator.adapter.secondary.out.SimpleDriversConfigurationRepository;
import giuseppe.pinto.transportation.aggregator.port.in.SearchUseCase;
import giuseppe.pinto.transportation.aggregator.port.in.SuppliersOutcomeService;
import giuseppe.pinto.transportation.aggregator.port.out.DriverConfigurationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TransportationAggregatorController.class})
public class TransportationAggregatorConfiguration {

    @Bean
    public DriverConfigurationRepository driverConfigurationRepository(){
        return new SimpleDriversConfigurationRepository(true);
    }

    @Bean
    public SuppliersOutcomeService tripsRepository(DriverConfigurationRepository driverConfigurationRepository){
        return new StandardSuppliersOutcomeService(driverConfigurationRepository);
    }

    @Bean
    public SearchUseCase searchTripsUseCase(SuppliersOutcomeService suppliersOutcomeService){
       return new StandardSearchUseCase(suppliersOutcomeService,
               new SolutionsAdapter(),
               new RequestAdapter());
    }

}
