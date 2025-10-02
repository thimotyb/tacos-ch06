package tacos.web.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;

import tacos.Taco;

@Configuration
public class SpringDataRestConfiguration {

  @Bean
  public RepresentationModelProcessor<PagedModel<EntityModel<Taco>>> tacoProcessor() {
    return model -> {
      model.add(linkTo(methodOn(RecentTacosController.class).recentTacos())
          .withRel("recents"));
      return model;
    };
  }
  
}
