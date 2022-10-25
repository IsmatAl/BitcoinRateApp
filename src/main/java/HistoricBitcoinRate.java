import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class HistoricBitcoinRate extends BitcoinRate {
    public Double minRate;
    public Double maxRate;

    public HistoricBitcoinRate(@JsonProperty("disclaimer") final String disclaimer) {
        super(disclaimer);
    }

    @JsonProperty("bpi")
    private void getBpi(Map<String, String> bpi) {
        this.minRate = bpi.values().stream()
                .mapToDouble(Double::valueOf)
                .min()
                .orElseThrow(IllegalStateException::new);

        this.maxRate = bpi.values().stream()
                .mapToDouble(Double::valueOf)
                .max()
                .orElseThrow(IllegalStateException::new);
    }
}
