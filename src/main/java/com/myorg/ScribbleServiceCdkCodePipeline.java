package com.myorg;

import static com.myorg.Constants.CODE_REPOSITORY_BRANCH;
import static com.myorg.Constants.SCRIBBLE_SERVICE_NAME;
import static com.myorg.Constants.SCRIBBLE_SERVICE_PIPELINE_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awscdk.services.codebuild.Project;
import software.amazon.awscdk.services.codecommit.Repository;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.Pipeline;
import software.amazon.awscdk.services.codepipeline.StageProps;
import software.amazon.awscdk.services.codepipeline.actions.CodeBuildAction;
import software.amazon.awscdk.services.codepipeline.actions.CodeCommitSourceAction;
import software.amazon.awscdk.services.codepipeline.actions.EcsDeployAction;
import software.amazon.awscdk.services.ecs.FargateService;
import software.constructs.Construct;

@RequiredArgsConstructor
public class ScribbleServiceCdkCodePipeline {

  private static final String SOURCE_STAGE_NAME = "Source";
  private static final String SOURCE_STAGE_ACTION_NAME = "CodeCommit";
  private static final String BUILD_STAGE_NAME = "Build";
  private static final String BUILD_STAGE_ACTION_NAME = "CodeBuild";
  private static final String DEPLOY_STAGE_NAME = "Deploy";
  private static final String DEPLOY_STAGE_ACTION_NAME = "ECS";

  private final Construct scope;
  @Getter
  private Pipeline pipeline;
  private Artifact sourceOutput, buildOutput;

  Pipeline createPipeline(final Repository repository, final Project project,
      final FargateService service) {
    StageProps sourceStage = createSourceStage(repository);
    StageProps buildStage = createBuildStage(project);
    StageProps deployStage = createDeployStage(service);
    List<StageProps> stages = Arrays.asList(sourceStage, buildStage, deployStage);

    pipeline = Pipeline.Builder
        .create(scope, SCRIBBLE_SERVICE_PIPELINE_ID)
        .pipelineName(SCRIBBLE_SERVICE_NAME)
        .stages(stages)
        .build();
    return pipeline;
  }

  private StageProps createSourceStage(final Repository repository) {
    sourceOutput = new Artifact();
    CodeCommitSourceAction action = CodeCommitSourceAction.Builder
        .create()
        .actionName(SOURCE_STAGE_ACTION_NAME)
        .repository(repository)
        .branch(CODE_REPOSITORY_BRANCH)
        .output(sourceOutput)
        .build();
    return StageProps.builder()
        .stageName(SOURCE_STAGE_NAME)
        .actions(Collections.singletonList(action))
        .build();
  }

  private StageProps createBuildStage(final Project project) {
    buildOutput = new Artifact();
    CodeBuildAction action = CodeBuildAction.Builder
        .create()
        .actionName(BUILD_STAGE_ACTION_NAME)
        .project(project)
        .input(sourceOutput)
        .outputs(Collections.singletonList(buildOutput))
        .build();
    return StageProps.builder()
        .stageName(BUILD_STAGE_NAME)
        .actions(Collections.singletonList(action))
        .build();
  }

  private StageProps createDeployStage(final FargateService service) {
    EcsDeployAction action = EcsDeployAction.Builder
        .create()
        .actionName(DEPLOY_STAGE_ACTION_NAME)
        .service(service)
        .input(buildOutput)
        .build();
    return StageProps.builder()
        .stageName(DEPLOY_STAGE_NAME)
        .actions(Collections.singletonList(action))
        .build();
  }
}
