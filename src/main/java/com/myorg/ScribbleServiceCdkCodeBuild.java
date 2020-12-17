package com.myorg;

import static com.myorg.Constants.CONTAINER_NAME;
import static com.myorg.Constants.IMAGE_REPOSITORY_NAME;
import static com.myorg.Constants.IMAGE_TAG;
import static com.myorg.Constants.SCRIBBLE_SERVICE_BUILD_PROJECT_ID;
import static com.myorg.Constants.SCRIBBLE_SERVICE_NAME;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.codebuild.BuildEnvironment;
import software.amazon.awscdk.services.codebuild.BuildEnvironmentVariable;
import software.amazon.awscdk.services.codebuild.BuildEnvironmentVariableType;
import software.amazon.awscdk.services.codebuild.LinuxBuildImage;
import software.amazon.awscdk.services.codebuild.PipelineProject;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.constructs.Construct;

@RequiredArgsConstructor
public class ScribbleServiceCdkCodeBuild {

  private static final String ECR_POWER_USER_POLICY_NAME = "AmazonEC2ContainerRegistryPowerUser";

  private final Construct scope;
  private final Map<String, BuildEnvironmentVariable> environmentVariables = new HashMap<>();
  @Getter
  private PipelineProject project;

  PipelineProject createProject() {
    Stack stack = Stack.of(scope);
    String account = stack.getAccount();
    addEnvironmentVariable("AWS_ACCOUNT_ID", account);
    addEnvironmentVariable("IMAGE_REPO_NAME", IMAGE_REPOSITORY_NAME);
    addEnvironmentVariable("IMAGE_TAG", IMAGE_TAG);
    addEnvironmentVariable("CONTAINER_NAME", CONTAINER_NAME);

    BuildEnvironment environment = BuildEnvironment.builder()
        .buildImage(LinuxBuildImage.STANDARD_4_0)
        .privileged(true)
        .environmentVariables(environmentVariables)
        .build();

    project = PipelineProject.Builder
        .create(scope, SCRIBBLE_SERVICE_BUILD_PROJECT_ID)
        .projectName(SCRIBBLE_SERVICE_NAME)
        .environment(environment)
        .build();
    return project;
  }

  void addEcrPermissionToRole() {
    Objects.requireNonNull(project.getRole()).addManagedPolicy(
        ManagedPolicy.fromAwsManagedPolicyName(ECR_POWER_USER_POLICY_NAME));
  }

  private void addEnvironmentVariable(final String key, final String value) {
    BuildEnvironmentVariable environmentVariable = BuildEnvironmentVariable.builder()
        .type(BuildEnvironmentVariableType.PLAINTEXT)
        .value(value)
        .build();
    environmentVariables.put(key, environmentVariable);
  }
}
