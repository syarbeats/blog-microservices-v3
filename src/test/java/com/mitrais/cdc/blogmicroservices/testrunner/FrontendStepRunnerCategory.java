package com.mitrais.cdc.blogmicroservices.testrunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features= "features/frontend/category", glue= "com.mitrais.cdc.blogmicroservices.frontendstepdefinition.category")
public class FrontendStepRunnerCategory {
}

