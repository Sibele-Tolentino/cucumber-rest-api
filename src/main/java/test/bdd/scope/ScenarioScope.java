package test.bdd.scope;

import java.util.HashMap;
import java.util.Map;

public class ScenarioScope {

    private Map<String,Object> headers;

    private Map<String,Object> jsonPaths;

    public ScenarioScope() {
        headers = new HashMap<>();
        jsonPaths = new HashMap<>();
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Map<String, Object> getJsonPaths() {
        return jsonPaths;
    }


    public boolean checkProperty(String property, String value) {
        Object headerValue = headers.get(property);
        boolean isHeader = headerValue != null && headerValue.equals(value);

        Object jsonPathValue = jsonPaths.get(property);
        boolean isJsonPath = jsonPathValue != null && jsonPathValue.equals(value);

        return isHeader || isJsonPath;
    }
}
