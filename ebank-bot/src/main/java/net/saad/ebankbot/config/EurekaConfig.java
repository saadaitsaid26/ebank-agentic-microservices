package net.saad.ebankbot.config;

import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import org.springframework.cloud.netflix.eureka.TimeoutProperties;
import org.springframework.cloud.netflix.eureka.http.DefaultEurekaClientHttpRequestFactorySupplier;
import org.springframework.cloud.netflix.eureka.http.RestClientDiscoveryClientOptionalArgs;
import org.springframework.cloud.netflix.eureka.http.RestClientTransportClientFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.Collections;

@Configuration
public class EurekaConfig {

    @Bean
    public TransportClientFactories<?> transportClientFactories() {
        DefaultEurekaClientHttpRequestFactorySupplier supplier =
                new DefaultEurekaClientHttpRequestFactorySupplier(
                        new TimeoutProperties(),
                        Collections.emptySet()
                );

        RestClientDiscoveryClientOptionalArgs args = new RestClientDiscoveryClientOptionalArgs(
                supplier,
                RestClient::builder
        );

        return new RestClientTransportClientFactories(args);
    }
}