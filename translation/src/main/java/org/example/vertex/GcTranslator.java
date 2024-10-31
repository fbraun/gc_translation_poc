package org.example.vertex;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GcTranslator {

    public static void main(String[] args) {
        // Set the original text and target language
        String originalText = "Newcastle United to Win and Both Teams To Score";
        String targetLanguage = "de";

        // Replace "your-project-id" with your actual project ID
        String projectId = "vertex-translation";
        ContextualTranslationService translationService = new ContextualTranslationService(projectId);

        // Translate text with context applied
        String translatedText = translationService.translateText(originalText, targetLanguage);

        // Print the translated text
        System.out.println("Original Text: " + originalText);
        System.out.println("Target Language: " + targetLanguage);
        System.out.println("Translated Text: " + translatedText);
    }
}

class TranslationService {
    private final Translate translate;

    public TranslationService(String projectId) {
        this.translate = TranslateOptions.newBuilder().setProjectId(projectId).build().getService();
//        this.translate = TranslateOptions.getDefaultInstance().getService();
    }

    public String translateText(String text, String targetLanguage) {
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.targetLanguage(targetLanguage),
                Translate.TranslateOption.model("nmt") // Neural Machine Translation model
        );
        return translation.getTranslatedText();
    }
}

class ContextualTranslationService extends TranslationService {
    private final Map<String, String> contextDictionary;

    public ContextualTranslationService(String projectId) {
        super(projectId);
        this.contextDictionary = createContextDictionary();
    }

    @Override
    public String translateText(String text, String targetLanguage) {
        // Pre-process text using the context dictionary
        String processedText = applyContextDictionary(text);

        // Translate the modified text
        return super.translateText(processedText, targetLanguage);
    }

    private String applyContextDictionary(String text) {
        for (Map.Entry<String, String> entry : contextDictionary.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return text;
    }

    private Map<String, String> createContextDictionary() {
        Map<String, String> contextDictionary = new HashMap<>();
        contextDictionary.put("Both Teams To Score", "beide Teams treffen");
        // Add more context-specific translations as needed
        return Collections.unmodifiableMap(contextDictionary);  // Make it unmodifiable
    }
}
