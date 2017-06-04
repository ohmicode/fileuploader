package ohmicode.example;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class GuideClient {

    private static final String SERVER_URL = "http://localhost:8080/ws-mobile/";
    private static final String USER_NAME  = GuideSetting.USER_NAME;
    private static final String USER_PASS  = GuideSetting.USER_PASS;

    public void sendGuideInput(String jsonBody) throws Exception {
        String sessionId = authorize();
        ClientResponse<String> response = serverPost(SERVER_URL + "submitdata", jsonBody, sessionId);
        if(response.getResponseStatus() != Response.Status.OK) {
            throw new RuntimeException("Not OK response: " + response.getResponseStatus());
        }
    }

    private String authorize() throws Exception {
        String authUserString = String.format("auth?login=%s&password=%s", USER_NAME, USER_PASS);
        ClientResponse<String> response = serverGet(SERVER_URL, authUserString, null);
        MultivaluedMap<String, String> headers = response.getHeaders();
        return headers.get("session_id").get(0);
    }

    private ClientResponse<String> serverGet(String url, String params, String sessionId) throws Exception {
        ClientRequest clRequest = new ClientRequest(url + params);
        if(sessionId != null) {
            clRequest.header("session_id", sessionId);
        }
        ClientResponse<String> response = clRequest.get(String.class);
        return response;
    }

    private static ClientResponse<String> serverPost(String url, String body, String sessionId) throws Exception {
        ClientRequest clRequest = new ClientRequest(url);
        clRequest.body(MediaType.APPLICATION_JSON_TYPE, body);
        clRequest.header("session_id", sessionId);
        ClientResponse<String> response = clRequest.post(String.class);
        return response;
    }
}
