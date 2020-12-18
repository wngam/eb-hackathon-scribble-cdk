package com.myorg;

import static com.myorg.Constants.CONTAINER_NAME;
import static com.myorg.Constants.IMAGE_TAG;
import static com.myorg.Constants.SCRIBBLE_SERVICE_ECS_SERVICE_ID;
import static com.myorg.Constants.SCRIBBLE_SERVICE_SCALING_POLICY_ID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.services.applicationautoscaling.EnableScalingProps;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.FargateService;
import software.amazon.awscdk.services.ecs.RequestCountScalingProps;
import software.amazon.awscdk.services.ecs.ScalableTaskCount;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationTargetGroup;
import software.constructs.Construct;

@RequiredArgsConstructor
public class ScribbleServiceCdkEcs {

  private static final int MIN_CAPACITY = 1;
  private static final int MAX_CAPACITY = 5;
  private static final int REQUESTS_PER_TARGET = 50;
  private static final int SCALE_OUT_COOLDOWN_IN_MINUTES = 5;
  private static final int SCALE_IN_COOLDOWN_IN_MINUTES = 5;

  private final Construct scope;
  @Getter
  private ApplicationLoadBalancedFargateService service;

  ApplicationLoadBalancedFargateService createEcsService(final Repository repository) {
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
    enableAutoScaling();
    return service;
  }

  private void enableAutoScaling() {
    FargateService fargateService = service.getService();
    EnableScalingProps enableScalingProps = EnableScalingProps.builder()
        .minCapacity(MIN_CAPACITY)
        .maxCapacity(MAX_CAPACITY)
        .build();
    ScalableTaskCount scalableTaskCount = fargateService.autoScaleTaskCount(enableScalingProps);

    ApplicationTargetGroup targetGroup = service.getTargetGroup();
    RequestCountScalingProps requestCountScalingProps = RequestCountScalingProps.builder()
        .targetGroup(targetGroup)
        .requestsPerTarget(REQUESTS_PER_TARGET)
        .scaleOutCooldown(Duration.minutes(SCALE_OUT_COOLDOWN_IN_MINUTES))
        .scaleInCooldown(Duration.minutes(SCALE_IN_COOLDOWN_IN_MINUTES))
        .build();
    scalableTaskCount
        .scaleOnRequestCount(SCRIBBLE_SERVICE_SCALING_POLICY_ID, requestCountScalingProps);
  }
}
