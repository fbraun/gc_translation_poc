package org.example.gemini;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

public class GeminiTranslator {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

    public static void main(String[] args) throws Exception {
        String apiKey = "";
        String originalText = "Newcastle United to Win and Both Teams To Score";
        String targetLanguage = "German";
        String context = "Context is sports betting. the term will be shown on a sports betting website. Only return the translated string.";

        String translatedText = translate(originalText, targetLanguage, context, apiKey);
        System.out.println("Original Text: " + originalText);
        System.out.println("Target Language: " + targetLanguage);
        System.out.println("Translated Text: " + translatedText);
    }

    public static String translate(String text, String targetLanguage, String context, String apiKey) throws Exception {
        URL url = new URL(API_URL + "?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = String.format("{\"contents\":[{\"parts\":[{\"text\":\"Translate the following text from English to %s: '%s'. The context is: %s\"}]}]}",
                targetLanguage, text, context);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }

            return parseTranslatedText(response.toString());
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        } finally {
            conn.disconnect();
        }
    }

    private static String parseTranslatedText(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray candidatesArray = jsonObject.getJSONArray("candidates");
        JSONObject firstCandidate = candidatesArray.getJSONObject(0);
        JSONObject contentObject = firstCandidate.getJSONObject("content");
        JSONArray partsArray = contentObject.getJSONArray("parts");
        JSONObject firstPart = partsArray.getJSONObject(0);
        return firstPart.getString("text");
    }
}
