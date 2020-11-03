package test.bdd.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import test.bdd.scope.ScenarioScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@SuppressWarnings("unchecked")
abstract class MethodsStepsDefinitions {


    String baseUri;

    Map<String, Object> body;

    private RestTemplate template;

    private HttpHeaders headers;

    private Map<String, String> queryParams;

    private ResponseEntity<String> responseEntity;
    private ObjectMapper objectMapper;

    private ScenarioScope scenarioScope;

    MethodsStepsDefinitions() {
        template = new RestTemplate();
        objectMapper = new ObjectMapper();
        scenarioScope = new ScenarioScope();
        headers = new HttpHeaders();
        queryParams = new HashMap<>();
    }

    void setBody(String body) throws IOException {
        Assert.notNull(body);
        Assert.isTrue(!body.isEmpty());
        this.body = objectMapper.readValue(body, Map.class);
    }

    void request(String resource, HttpMethod method) {
        Assert.notNull(resource);
        Assert.isTrue(!resource.isEmpty());

        Assert.notNull(method);

        boolean writeMode = !HttpMethod.GET.equals(method)
                && !HttpMethod.DELETE.equals(method)
                && !HttpMethod.OPTIONS.equals(method)
                && !HttpMethod.HEAD.equals(method);

        if(!resource.contains("/")) {
            resource = "/" + resource;
        }
        HttpEntity httpEntity;

        if(writeMode) {
            Assert.notNull(body);
            httpEntity = new HttpEntity(body, headers);
        } else {
            httpEntity = new HttpEntity(headers);
        }

        responseEntity = this.template.exchange(baseUri+resource, method, httpEntity, String.class,queryParams);
        Assert.notNull(responseEntity);
    }

    void requestId(String resource, HttpMethod method, String jsonPath) {
        Assert.notNull(resource);
        Assert.isTrue(!resource.isEmpty());

        Assert.notNull(method);

        boolean writeMode = !HttpMethod.GET.equals(method)
                && !HttpMethod.DELETE.equals(method)
                && !HttpMethod.OPTIONS.equals(method)
                && !HttpMethod.HEAD.equals(method);

        if(!resource.contains("/")) {
            resource = "/" + resource;
        }
        
        HttpEntity httpEntity;
        
        if(writeMode) {
            Assert.notNull(body);
            httpEntity = new HttpEntity(body, headers);
        } else {
            httpEntity = new HttpEntity(headers);
        }
        Object pathValue = getJsonPath(jsonPath);
        
        responseEntity = this.template.exchange(baseUri+resource+pathValue, method, httpEntity, String.class,queryParams);
        Assert.notNull(responseEntity);
    }

    void checkStatus(int status, boolean isNot){
        Assert.isTrue(status > 0);
        Assert.isTrue(isNot ? responseEntity.getStatusCodeValue() != status : responseEntity.getStatusCodeValue() == status);
    }


    List<String> checkHeaderExists(String headerName, boolean isNot){
        Assert.notNull(headerName);
        Assert.isTrue(!headerName.isEmpty());
        Assert.notNull(responseEntity.getHeaders());
        if(!isNot) {
            Assert.notNull(responseEntity.getHeaders().get(headerName));
            return responseEntity.getHeaders().get(headerName);
        } else {
            Assert.isNull(responseEntity.getHeaders().get(headerName));
            return null;
        }
    }

    void checkJsonBody() throws IOException {
        String body = responseEntity.getBody();
        Assert.notNull(body);
        Assert.isTrue(!body.isEmpty());
        objectMapper.readValue(body,Map.class);
    }

    void checkJsonBodySimulations() throws IOException {
        String body = responseEntity.getBody();
        Assert.notNull(body);
        Assert.isTrue(!body.isEmpty());
    }

    void checkBodyContains(String bodyValue) {
        Assert.notNull(bodyValue);
        Assert.isTrue(!bodyValue.isEmpty());

        Assert.isTrue(responseEntity.getBody().contains(bodyValue));
    }

    Object checkJsonPathExists(String jsonPath){
        return getJsonPath(jsonPath);
    }

    void checkJsonPath(String jsonPath, String jsonValue, boolean isNot){
        Object pathValue = checkJsonPathExists(jsonPath);
        Assert.isTrue(!String.valueOf(pathValue).isEmpty());

        if(!isNot) {
            Assert.isTrue(pathValue.equals(jsonValue));
        } else {
            Assert.isTrue(!pathValue.equals(jsonValue));
        }
    }

    private ReadContext getBodyDocument(){
        ReadContext ctx = JsonPath.parse(responseEntity.getBody());
        Assert.notNull(ctx);

        return ctx;
    }

    private Object getJsonPath(String jsonPath){

        Assert.notNull(jsonPath);
        Assert.isTrue(!jsonPath.isEmpty());

        ReadContext ctx = getBodyDocument();
        Object pathValue = ctx.read(jsonPath);

        Assert.notNull(pathValue);

        return pathValue;
    }
}
