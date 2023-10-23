package co.com.ajac.api.messaging.listenerprovides.aws.dynamo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.immutables.value.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

import java.time.Instant;

@Value
@Builder
@DynamoDbImmutable(builder = ListenerModelSchema.ListenerModelSchemaBuilder.class)
class ListenerModelSchema {

    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private String listener;

    @Getter
    private String pathClass;

    @Getter
    private String state;

    @Getter(onMethod_=@DynamoDbSecondarySortKey(indexNames = {"listener_model_by_date"}))
    private Instant createdDate;
}
