package animals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class Loader {
    public static TreeNode readJSON(String type) throws IOException {

        ObjectMapper objectMapper = getObjectMapper(type);
        String fileName = getFileName(type);

        TreeNode root = objectMapper
                    .readValue(new File(fileName), TreeNode.class);

        return root;
    }

    public static void writeJSON(TreeNode root, String type) throws IOException {
        ObjectMapper objectMapper = getObjectMapper(type);
        String fileName =getFileName(type);

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(new File(fileName), root);

    }

    private static ObjectMapper getObjectMapper(String type) {
        ObjectMapper objectMapper;
        switch (type) {
            case "xml" :{
                objectMapper = new XmlMapper();
                break;
            }
            case "yaml" :{
                objectMapper = new YAMLMapper();
                break;
            }
            default: {
                objectMapper = new JsonMapper();
                break;
            }
        }
        return objectMapper;
    }

    private static String getFileName(String type) {
        String fileName;
        switch (type) {
            case "xml" :{
                fileName = "animals"
                        + Application.rBPatterns.getString("file")
                        + ".xml";
                break;
            }
            case "yaml" :{
                fileName = "animals"
                        + Application.rBPatterns.getString("file")
                        + ".yaml";
                break;
            }
            default: {
                fileName = "animals"
                        + Application.rBPatterns.getString("file")
                        + ".json";
                break;
            }
        }
        return fileName;
    }
}
