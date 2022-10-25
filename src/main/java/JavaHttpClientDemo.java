import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class JavaHttpClientDemo {

    private static final String historicBitcoinRateURI = "https://api.coindesk.com/v1/bpi/historical/close.json?start=%1$s&end=%2$s&currency=%3$s";
    private static final String currentBitcoinRateURI = "https://api.coindesk.com/v1/bpi/currentprice/%s.json";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.print("Currency code: ");

        String currencyCode = myObj.nextLine();  // Read user input
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(180);

        HistoricBitcoinRate historicBitcoinRate;
        CurrentBitcoinRate currentBitcoinRate;

        try {
            historicBitcoinRate = asynchronousRequest(String.format(historicBitcoinRateURI, startDate, endDate, currencyCode), HistoricBitcoinRate.class);
            currentBitcoinRate = asynchronousRequest(String.format(currentBitcoinRateURI, currencyCode.toLowerCase()), CurrentBitcoinRate.class);

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
            Currency currency = Currency.getInstance(currencyCode);
            numberFormat.setCurrency(currency);

            System.out.println(
                    "The current Bitcoin rate: " + numberFormat.format(currentBitcoinRate.getRates().get(currencyCode).getRate()) + "\n" +
                            "The lowest Bitcoin rate in the last 180 days: " + numberFormat.format(historicBitcoinRate.getMinRate()) + "\n" +
                            "The highest Bitcoin rate in the last 180 days: " + numberFormat.format(historicBitcoinRate.getMaxRate()));

        } catch (IllegalArgumentException e) {
            System.out.printf("The currency code\"%s\" is not supported", currencyCode);
        }
    }

    private static <T> T asynchronousRequest(String uri, Class<T> targetType) throws InterruptedException, ExecutionException {
        // create a client
        var client = HttpClient.newHttpClient();

        // create a request
        var request = HttpRequest.newBuilder(
                        URI.create(uri))
                .header("accept", "application/json")
                .build();

        // use the client to send the request
        var responseFuture = client.sendAsync(request, new JsonBodyHandler<>(targetType));
        // This blocks until the request is complete
        var response = responseFuture.get();

        // the response:
        return response.body().get();
    }
}