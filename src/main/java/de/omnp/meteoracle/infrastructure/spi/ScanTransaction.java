package de.omnp.meteoracle.infrastructure.spi;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// import de.omnp.meteoracle.application.domain.ddd.DomainService;
import de.omnp.meteoracle.application.domain.vda4994.Post;
import de.omnp.meteoracle.application.port.TransactionInterface;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tools.jackson.databind.ObjectMapper;

@Service
public class ScanTransaction implements TransactionInterface {

    String rpc_url = new String("https://api.testnet.iota.cafe:443");

    String jsonBody = new String(
        "{\n" +
        "  \"jsonrpc\": \"2.0\",\n" +
        "  \"id\": 1,\n" +
        "  \"method\": \"iotax_getAllBalances\",\n" +
        "  \"params\": [\n" +
        "    \"0x9b5131e572a876af76dde3a98194bce54d7ba4938531d609b23b18e94c4bb8ff\"\n" +
        "  ]\n" +
        "}"
    );

    private Logger logger = LoggerFactory.getLogger(getClass());

    // Preparing the object and metadata
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MediaType mediaType = MediaType.parse("application/json");
    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .build();

    public boolean sendTransaction(Post object) {
        
        // TODO: Sending into the iota network.       
        // RequestBody body = RequestBody.create(objectMapper.writeValueAsString(object), mediaType);
        RequestBody body = RequestBody.create(jsonBody, mediaType);
                
        Request request = new Request.Builder()
            .url(rpc_url)
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build();
        
        // TODO: Später auskommentieren
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        
        // TODO: So konfigurieren, dass hier nicht mehr auskommentiert werden muss, um slice test durchzuführen (ohne transffer ins netzwerk)

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                logger.info("Successfully sent message from {} into the network from the location {}", object.getDeviceId(), new String(String.valueOf(object.getLocation().getLatitude()) + " and " + String.valueOf(object.getLocation().getLongitude())));
                logger.info("Response from server: " + response.body().string());
                return true;
            } else {
                logger.error("Error sending the transaction into the network. RPC node response code: " + response.code());
                return false;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}
