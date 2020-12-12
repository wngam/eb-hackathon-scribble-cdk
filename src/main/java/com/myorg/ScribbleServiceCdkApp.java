package com.myorg;

import software.amazon.awscdk.core.App;

import java.util.Arrays;

public class ScribbleServiceCdkApp {
    public static void main(final String[] args) {
        App app = new App();

        new ScribbleServiceCdkStack(app, "ScribbleServiceCdkStack");

        app.synth();
    }
}
