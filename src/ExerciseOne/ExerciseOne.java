package ExerciseOne;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class ExerciseOne {
    private static final String TEST_URL =
            "https://jsonplaceholder.typicode.com/users";
    public static void main(String[] args) throws IOException {
//        sendGET();
//        sendPOST();
//        sendDELETE(1);
//        sendGETID(1);
//        sendPUT();
//        sendGETUSERNAME();
//        sendGETTODOS(1);
        sendGETCOMMENTSID();
    }
    private static void sendPOST() throws IOException{
        URL url = new URL(TEST_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(Files.readAllBytes(new File("user.json").toPath()));
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        System.out.println("POST response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }

    private static  void sendPUT () throws IOException {
        URL url = new URL(TEST_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(Files.readAllBytes(new File("user.json").toPath()));
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        System.out.println("PUT response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
            System.out.println("PUT request not worked");
        }
    }
    private static void sendDELETE(int userId) throws IOException {

        String urlString = TEST_URL + "/" + userId;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        System.out.println("DELETE response code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Пользователь с userId " + userId + "удален");
        } else {
            System.out.println("DELETE-запрос не сработал");
        }
    }

    private static void sendGET() throws IOException {
        URL url = new URL(TEST_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
        }
    }
    private static void sendGETID(int userId) throws IOException {
        URL url = new URL(TEST_URL + "/" + userId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GETID response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());

        } else {
            System.out.println("GETID request not worked");
        }
    }
    private static void sendGETUSERNAME(String username) throws IOException {
        URL url = new URL(TEST_URL + "?username=" + username);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GETUSERNAME response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
            System.out.println("GETUSERNAME request not worked");
        }
    }
    private static void sendGETCOMMENTSID() throws IOException {
        try {
            String url = TEST_URL +"/1/posts";

            JSONArray posts = sendGetRequest(url);

            if (posts.isEmpty()) {
                System.out.println("Постов пользователя не найдено.");
                return;
            }
            JSONObject lastPost = null;
            long maxId = -1;

            for (Object obj : posts) {
                JSONObject post = (JSONObject) obj;
                long postId = Long.parseLong(post.get("id").toString());

                if (postId > maxId) {
                    maxId = postId;
                    lastPost = post;
                }
            }
            int postId = Integer.parseInt(lastPost.get("id").toString());
            String commentsUrl = "https://jsonplaceholder.typicode.com/posts/" + postId + "/comments";
            JSONArray comments = sendGetRequest(commentsUrl);
            String fileName = "user-1-post-" + postId + "-comments.json";
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                fileWriter.write(comments.toJSONString());
                System.out.println("Comments are given in the file " + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONArray sendGetRequest(String url) throws Exception {
        URL testUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) testUrl.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return (JSONArray) new JSONParser().parse(response.toString());
        } else {
            throw new IOException("GET request failed with response code: " + responseCode);
        }
    }
   private static void sendGETTODOS(int userId) throws IOException, ParseException {
        URL url = new URL(TEST_URL + "/" + userId + "/todos");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GETUSERNAME response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONParser parser = new JSONParser();
            JSONArray todos = (JSONArray) parser.parse(response.toString());

            for (Object obj : todos) {
                JSONObject todo = (JSONObject) obj;
                boolean completed = (Boolean) todo.get("completed");
                if (completed) {
                    System.out.println("Task ID: " + todo.get("id") + ", Title: " + todo.get("title"));
                }
            }
        }  else {
            System.out.println("GET request not worked");
        }

    }
}


