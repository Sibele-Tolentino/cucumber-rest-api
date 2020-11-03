package test.bdd.glue;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;


@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StepsDefinitionsTest extends MethodsStepsDefinitions {


    @Given("^baseUri is (.*)$")
    public void baseUri(String uri) {
        Assert.notNull(uri);
        Assert.isTrue(!uri.isEmpty());
        baseUri = uri;
    }

    @Given("^I set body to (.*)$")
    public void setBodyTo(String body) throws IOException {
        this.setBody(body);
    }


    @When("^I GET (.*)$")
    public void get(String resource) {
        this.request(resource, HttpMethod.GET);
    }

    @When("^I POST (.*)$")
    public void post(String resource) {
        this.request(resource, HttpMethod.POST);
    }

    @When("^I PUT (.*)$")
    public void put(String resource) {
        this.request(resource, HttpMethod.PUT);
    }

    @Then("^response code should be (\\d+)$")
    public void responseCode(Integer status) {
        this.checkStatus(status, false);
    }


    @Then("^response body should be valid json$")
    public void bodyIsValid() throws IOException {
        this.checkJsonBody();
    }

    @Then("^response body should be valid simulations json$")
    public void bodySimulatonsIsValid() throws IOException {
        this.checkJsonBodySimulations();
    }


    @Then("^response body path (.*) should exists$")
    public void bodyPathExists(String jsonPath) throws IOException {
        this.checkJsonPathExists(jsonPath);
    }


    @Then("^response body should contain (.*)$")
    public void bodyContains(String bodyValue) throws IOException {
        this.checkBodyContains(bodyValue);
    }

    @Then("^I DELETE simulation (.*) with (.*)$")
    public void delete(String resource, String jsonPath) {
        this.requestId(resource, HttpMethod.DELETE, jsonPath);
    }
 

}
