package transaction.authorizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import transaction.authorizer.consumer.AuthorizerConsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;

public class AuthorizerApplication {

    private static final String INPUT_OPERATIONS_PATH = "./src/main/resources/operations";
    private static final String OUTPUT_OPERATIONS_PATH = "./output";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        setupObjectMapper();
        processOperations();
    }

    private static void setupObjectMapper() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    private static void processOperations() throws IOException {
        final Path input = Path.of(INPUT_OPERATIONS_PATH);

        try (BufferedReader reader = Files.newBufferedReader(input)) {
            authorizeOperation(reader);
        }
    }

    private static void authorizeOperation(BufferedReader reader) throws IOException {
        final Path output = Path.of(OUTPUT_OPERATIONS_PATH);
        final AuthorizerConsumer authorizerConsumer = AuthorizerConsumer.getInstance();
        String operationLine = "";

        try (Writer writer = Files.newBufferedWriter(output)) {
            while ((operationLine = reader.readLine()) != null) {
                JsonNode operationNode = OBJECT_MAPPER.readValue(operationLine, JsonNode.class);

                if (operationNode.get("account") != null) {
                    final boolean activeCard = operationNode.get("account").get("active-card").asBoolean();
                    final int availableLimit = operationNode.get("account").get("available-limit").asInt();

                    writer.write(authorizerConsumer.processAccount(activeCard, availableLimit));
                }

                if (operationNode.get("transaction") != null) {
                    final String merchant = operationNode.get("transaction").get("merchant").asText();
                    final int amount = operationNode.get("transaction").get("amount").asInt();
                    final ZonedDateTime time = ZonedDateTime
                            .parse(operationNode.get("transaction").get("time").asText());

                    writer.write(authorizerConsumer.processTransaction(merchant, amount, time));
                }
            }
        }
    }
}
