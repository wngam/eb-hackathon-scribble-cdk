package com.myorg;

import static com.myorg.Constants.IMAGE_REPOSITORY_NAME;
import static com.myorg.Constants.SCRIBBLE_SERVICE_IMAGE_REPOSITORY_ID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.services.ecr.Repository;
import software.constructs.Construct;

@RequiredArgsConstructor
public class ScribbleServiceCdkEcr {

  private final Construct scope;
  @Getter
  private Repository repository;

  Repository createRepository() {
    repository = Repository.Builder
        .create(scope, SCRIBBLE_SERVICE_IMAGE_REPOSITORY_ID)
        .repositoryName(IMAGE_REPOSITORY_NAME)
        .removalPolicy(RemovalPolicy.DESTROY)
        .build();
    return repository;
  }
}
