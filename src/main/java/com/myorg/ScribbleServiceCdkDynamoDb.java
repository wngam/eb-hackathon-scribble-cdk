package com.myorg;

import static com.myorg.Constants.PLAYER_TABLE_NAME;
import static com.myorg.Constants.SCRIBBLE_SERVICE_PLAYER_TABLE_ID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.iam.IGrantable;
import software.constructs.Construct;

@RequiredArgsConstructor
public class ScribbleServiceCdkDynamoDb {

  private static final String PARTITION_KEY_NAME = "Name";

  private final Construct scope;
  @Getter
  private Table table;

  Table createTable() {
    Attribute partitionKey = Attribute.builder()
        .name(PARTITION_KEY_NAME)
        .type(AttributeType.STRING)
        .build();

    table = Table.Builder
        .create(scope, SCRIBBLE_SERVICE_PLAYER_TABLE_ID)
        .tableName(PLAYER_TABLE_NAME)
        .partitionKey(partitionKey)
        .removalPolicy(RemovalPolicy.DESTROY)
        .build();
    return table;
  }

  void grantReadWriteDataPermission(final IGrantable grantee) {
    table.grantReadWriteData(grantee);
  }
}
