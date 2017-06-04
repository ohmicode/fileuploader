package ohmicode.example;

import org.json.simple.JSONObject;

import java.io.IOException;

public class Upload {

    public static void main(String[] args) {
        if(args.length > 0) {
            String requestBody = buildJsonString(args);
            System.out.println("Sending data: " + requestBody);

            sendDataToServer(requestBody);
            System.out.println("Done.");
        } else {
            System.out.println("Please provide file name(s)");
        }
    }

    private static String buildJsonString(String[] fileNames) {
        JsonBuilder builder = new JsonBuilder();
        for(String fileName : fileNames) {
            try {
                builder.addFile(fileName);
            } catch (IOException ioe) {
                System.out.printf("Problem with appending file %s: %s%n", fileName, ioe.getMessage());
                ioe.printStackTrace();
            }
        }
        JSONObject json = builder.build();
        return json.toString();
    }

    private static void sendDataToServer(String requestBody) {
        try {
            GuideClient client = new GuideClient();
            client.sendGuideInput(requestBody);
        } catch (Exception e) {
            System.out.println("Error on uploading: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
