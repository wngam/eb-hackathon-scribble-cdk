package com.myorg;

public class Constants {

  static final String SCRIBBLE_SERVICE_NAME = "ScribbleService";
  static final String SCRIBBLE_SERVICE_CODE_REPOSITORY_ID = SCRIBBLE_SERVICE_NAME + "Repository";
  static final String SCRIBBLE_SERVICE_IMAGE_REPOSITORY_ID =
      SCRIBBLE_SERVICE_NAME + "ImageRepository";
  static final String SCRIBBLE_SERVICE_BUILD_PROJECT_ID = SCRIBBLE_SERVICE_NAME + "BuildProject";
  static final String SCRIBBLE_SERVICE_PIPELINE_ID = SCRIBBLE_SERVICE_NAME + "Pipeline";
  static final String SCRIBBLE_SERVICE_ECS_SERVICE_ID = SCRIBBLE_SERVICE_NAME + "EcsService";

  static final String CODE_REPOSITORY_BRANCH = "master";

  static final String IMAGE_REPOSITORY_NAME = SCRIBBLE_SERVICE_NAME.toLowerCase();
  static final String IMAGE_TAG = "latest";

  static final String CONTAINER_NAME = SCRIBBLE_SERVICE_NAME.toLowerCase();
}
