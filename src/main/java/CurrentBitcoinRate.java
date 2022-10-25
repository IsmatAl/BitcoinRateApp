import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Getter
public class CurrentBitcoinRate extends BitcoinRate {


    private final Map<String, Rate> rates = new HashMap<>();

    @Getter
    class Rate {
        String code;
        Double rate;

        Rate(String code, Double rate) {
            this.code = code;
            this.rate = rate;
        }
    }

    public CurrentBitcoinRate(@JsonProperty("disclaimer") final String disclaimer) {
        super(disclaimer);
    }

    @JsonProperty("bpi")
    private void getBpi(Map<String, Map<String, String>> bpi) {
        bpi.forEach((key1, value) ->
                this.rates.put(key1, new Rate(value.get("code"), Double.valueOf(value.get("rate_float")))));
    }
}
