package fr.ubs.sporttrack.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class RandomTimeoutGatewayFilterFactory extends AbstractGatewayFilterFactory<RandomTimeoutGatewayFilterFactory.Config> {

    public RandomTimeoutGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public String name() {
        return "RandomTimeout";
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (Math.random() < 0.5) {
                return Mono.delay(Duration.ofSeconds(61))
                        .then(chain.filter(exchange));
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}