package ohmicode.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class JsonBuilder {

    private static final String GUIDE_ID   = GuideSetting.GUIDE_ID;
    private static final String VERSION_ID = GuideSetting.VERSION_ID;
    private static final String IMAGE_VARIABLE_NAME = GuideSetting.IMAGE_VARIABLE_NAME;
    private static final String AUDIO_VARIABLE_NAME = GuideSetting.AUDIO_VARIABLE_NAME;
    private static final String VIDEO_VARIABLE_NAME = GuideSetting.VIDEO_VARIABLE_NAME;
    private static final List<String> IMAGE_TYPES = Arrays.asList("bm", "bmp", "fif", "gif", "ief", "jpeg", "jpg", "naplps", "pict", "png", "tiff", "xbm", "xpm");
    private static final List<String> AUDIO_TYPES = Arrays.asList("aiff", "it", "midi", "mod", "mp3", "s3m", "voc", "wav", "xm");
    private static final List<String> VIDEO_TYPES = Arrays.asList("avi", "dl", "fli", "gl", "mpeg", "mp4", "vdo", "vivo", "x-flv");

    private JSONArray dataArray = new JSONArray();

    public void addFile(String fileName) throws IOException {
        JSONObject dataObject = new JSONObject();
        String variableName = getVariableName(fileName);
        String encodedValue = encodeFile(fileName);
        dataObject.put("input_id", variableName);
        dataObject.put("value", encodedValue);
        dataArray.add(dataObject);
    }

    public JSONObject build() {
        JSONObject rootJson = new JSONObject();
        rootJson.put("guide_id", GUIDE_ID);
        rootJson.put("guide_version", VERSION_ID);
        String dateString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss").format(LocalDateTime.now());
        rootJson.put("timestamp", dateString);
        rootJson.put("results", dataArray);
        return rootJson;
    }

    private String getVariableName(String fileName) {
        int dotIdx = fileName.lastIndexOf('.');
        String extension = fileName.substring(dotIdx+1);
        if(IMAGE_TYPES.contains(extension)) {
            return IMAGE_VARIABLE_NAME;
        } else if(AUDIO_TYPES.contains(extension)) {
            return AUDIO_VARIABLE_NAME;
        } else if(VIDEO_TYPES.contains(extension)) {
            return VIDEO_VARIABLE_NAME;
        }
        return extension;
    }

    private String encodeFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        byte[] data = Files.readAllBytes(path);
        String base64 = new String(Base64.encodeBase64(data));
        return base64;
    }
}
