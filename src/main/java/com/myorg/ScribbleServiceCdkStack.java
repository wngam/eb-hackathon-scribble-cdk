package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.codebuild.Project;
import software.amazon.awscdk.services.codecommit.Repository;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;

public class ScribbleServiceCdkStack extends Stack {

  public ScribbleServiceCdkStack(final Construct scope, final String id) {
    this(scope, id, null);
  }

  public ScribbleServiceCdkStack(final Construct scope, final String id, final StackProps props) {
    super(scope, id, props);

    // The code that defines your stack goes here
    ScribbleServiceCdkCodeCommit codeCommit = new ScribbleServiceCdkCodeCommit(this);
    ScribbleServiceCdkEcr ecr = new ScribbleServiceCdkEcr(this);
    ScribbleServiceCdkCodeBuild codeBuild = new ScribbleServiceCdkCodeBuild(this);
    ScribbleServiceCdkEcs ecs = new ScribbleServiceCdkEcs(this);
    ScribbleServiceCdkCodePipeline codePipeline = new ScribbleServiceCdkCodePipeline(this);
    ScribbleServiceCdkDynamoDb dynamoDb = new ScribbleServiceCdkDynamoDb(this);

    Repository codeRepository = codeCommit.createRepository();
    software.amazon.awscdk.services.ecr.Repository imageRepository = ecr.createRepository();
    Project project = codeBuild.createProject();
    codeBuild.addEcrPermissionToRole();
    ApplicationLoadBalancedFargateService service = ecs.createEcsService(imageRepository);
    codePipeline.createPipeline(codeRepository, project, service.getService());
    dynamoDb.createTable();
    dynamoDb.grantReadWriteDataPermission(service.getTaskDefinition().getTaskRole());
  }
}
