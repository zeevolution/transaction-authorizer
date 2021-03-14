package transaction.authorizer;

import transaction.authorizer.consumer.AuthorizerConsumer;

import java.time.ZonedDateTime;

public class AuthorizerApplication {

    public static void main(String[] args) {
        final AuthorizerConsumer authorizerConsumer = AuthorizerConsumer.getInstance();

        authorizerConsumer.processAccount(true, 500);
        authorizerConsumer.processTransaction("uber", 100, ZonedDateTime.now());
        authorizerConsumer.processTransaction("amazon", 100, ZonedDateTime.now());
        authorizerConsumer.processAccount(true, 400);
        authorizerConsumer.processTransaction("globoplay", 200, ZonedDateTime.now());
        authorizerConsumer.processTransaction("netflix", 400, ZonedDateTime.now());
    }
}
