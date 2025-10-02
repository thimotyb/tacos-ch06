package tacos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "taco.discount")
public class DiscountCodeProps {

  private Map<String, Integer> codes = new HashMap<>();

  public Map<String, Integer> getCodes() {
    return codes;
  }

  public void setCodes(Map<String, Integer> codes) {
    this.codes = codes != null ? new HashMap<>(codes) : new HashMap<>();
  }
}
