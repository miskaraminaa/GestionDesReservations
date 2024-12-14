package ma.ensa.reservation.config;

import lombok.AllArgsConstructor;
import ma.ensa.reservation.ws.ReservationSoapService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class CxfConfig {

    @Bean
    public ServletRegistrationBean<CXFServlet> cxfServlet() {
        return new ServletRegistrationBean<>(new CXFServlet(), "/services/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }
    @Bean
    public EndpointImpl reservationServiceEndpoint(ReservationSoapService reservationSoapService) {
        EndpointImpl endpoint = new EndpointImpl(springBus(), reservationSoapService);
        endpoint.publish("/reservation");
        return endpoint;
    }

}