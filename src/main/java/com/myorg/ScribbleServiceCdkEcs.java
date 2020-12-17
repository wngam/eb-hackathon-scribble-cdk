package com.myorg;

import static com.myorg.Constants.CONTAINER_NAME;
import static com.myorg.Constants.IMAGE_TAG;
import static com.myorg.Constants.SCRIBBLE_SERVICE_ECS_SERVICE_ID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.constructs.Construct;

@RequiredArgsConstructor
public class ScribbleServiceCdkEcs {

  private final Construct scope;
  @Getter
  private ApplicationLoadBalancedFargateService service;

  ApplicationLoadBalancedFargateService createEcsService(Repository repository) {
    ContainerImage image = ContainerImage.fromEcrRepository(repository, IMAGE_TAG);
    ApplicationLoadBalancedTaskImageOptions taskImageOptions = ApplicationLoadBalancedTaskImageOptions
        .builder()
        .containerName(CONTAINER_NAME)
        .image(image)
        .containerPort(8080)
        .build();

    service = ApplicationLoadBalancedFargateService.Builder
        .create(scope, SCRIBBLE_SERVICE_ECS_SERVICE_ID)
        .taskImageOptions(taskImageOptions)
        .build();
    return service;
  }
}
