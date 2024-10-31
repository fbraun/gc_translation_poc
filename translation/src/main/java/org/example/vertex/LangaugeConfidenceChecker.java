package org.example.vertex;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;

public class LangaugeConfidenceChecker {

    public static void main(String[] args) {
        // Example usage
        String text =  "Jatkoaika ja rangaistuslaukauskilpailu mukaanlukien";
        String targetLanguage = "fi";  // ISO-639-1 code for French
        float confidenceScore = detectLanguageConfidence(text, targetLanguage);
        System.out.println("Confidence score: " + confidenceScore);
    }

    public static float detectLanguageConfidence(String text, String targetLanguage) {
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        // Detect language of the input text
        Detection detection = translate.detect(text);

        // Check if the detected language matches the target language and return the confidence score
        if (detection.getLanguage().equals(targetLanguage)) {
            // Scale confidence to 0-100 range for easier interpretation
            return detection.getConfidence() * 100;
        } else {
            // Return 0 if language doesn't match
            return 0;
        }
    }
}
