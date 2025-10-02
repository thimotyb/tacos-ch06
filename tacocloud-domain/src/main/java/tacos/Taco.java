package tacos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.data.rest.core.annotation.RestResource;

@Entity
@RestResource(rel = "tacos", path = "tacos")
public class Taco {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Size(min = 5, message = "Name must be at least 5 characters long")
  private String name;

  private Date createdAt;

  @ManyToMany(targetEntity = Ingredient.class)
  @Size(min = 1, message = "You must choose at least 1 ingredient")
  private List<Ingredient> ingredients = new ArrayList<>();

  protected Taco() {
    // JPA requirement
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredient> ingredients) {
    this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
  }

  public void addIngredient(Ingredient ingredient) {
    if (ingredient != null) {
      this.ingredients.add(ingredient);
    }
  }

  @PrePersist
  void createdAt() {
    this.createdAt = new Date();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Taco)) {
      return false;
    }
    Taco other = (Taco) o;
    return Objects.equals(id, other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
