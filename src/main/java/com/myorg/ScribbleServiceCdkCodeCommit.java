package com.myorg;

import static com.myorg.Constants.SCRIBBLE_SERVICE_CODE_REPOSITORY_ID;
import static com.myorg.Constants.SCRIBBLE_SERVICE_NAME;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awscdk.services.codecommit.Repository;
import software.constructs.Construct;

@RequiredArgsConstructor
public class ScribbleServiceCdkCodeCommit {

  private final Construct scope;
  @Getter
  private Repository repository;

  Repository createRepository() {
    repository = Repository.Builder
        .create(scope, SCRIBBLE_SERVICE_CODE_REPOSITORY_ID)
        .repositoryName(SCRIBBLE_SERVICE_NAME)
        .build();
    return repository;
  }
}
