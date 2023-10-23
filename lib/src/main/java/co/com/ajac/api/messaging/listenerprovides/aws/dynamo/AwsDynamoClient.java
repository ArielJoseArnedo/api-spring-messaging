package co.com.ajac.api.messaging.listenerprovides.aws.dynamo;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Component
public class AwsDynamoClient {

    private final DynamoDbClient dynamoDbClient;

    public AwsDynamoClient() {
        this.dynamoDbClient = createDynamoDbClientDefault();
    }

    public AwsDynamoClient(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    private DynamoDbClient createDynamoDbClientDefault() {
        return DynamoDbClient.create();
    }

    public DynamoDbEnhancedClient getDynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
          .dynamoDbClient(dynamoDbClient)
          .build();
    }
}
