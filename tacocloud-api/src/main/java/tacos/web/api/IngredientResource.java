package tacos.web.api;

import org.springframework.hateoas.RepresentationModel;

import tacos.Ingredient;
import tacos.Ingredient.Type;

public class IngredientResource extends RepresentationModel<IngredientResource> {

  private final String name;
  private final Type type;

  public IngredientResource(Ingredient ingredient) {
    this.name = ingredient.getName();
    this.type = ingredient.getType();
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }
}
