import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class BitcoinRate {
    public final String disclaimer;
    public Map<String,String> time;

    public BitcoinRate(@JsonProperty("disclaimer") String disclaimer) {
        this.disclaimer = disclaimer;
    }

    @JsonProperty("time")
    private void getTime(Map<String,String> time) {
        this.time = time;
    }
}