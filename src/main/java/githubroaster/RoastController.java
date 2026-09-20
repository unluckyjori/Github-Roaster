package githubroaster;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@RestController
public class RoastController {
    // Create spring rest object to use for requests later
    private final RestClient client = RestClient.create();
    // Set gemini api key from secret
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @GetMapping("/roast")
    public String callback(@RequestParam String username) {
        return getGeminiRoast(requestGithubRepositories(username));
    }


    private String requestGithubRepositories(String username) {
        // Request the data
        String response = client.get()
        .uri("https://api.github.com/users/" + username + "/repos?per_page=100")
        .header("Accept", "application/vnd.github+json")
        .header("X-GitHub-Api-Version", "2026-03-10")
        .retrieve()
        .body(String.class);
        return response;
    }

    private String getGeminiRoast(String githubData) {
        // Use gemini api to get response
        String roastPrompt = "You are a brutally honest comedian AI and your job is to roast the user based on their github repositories they have coded. Do NOT be polite. Do NOT give advice. Do NOT compliment. The more funny and more personal your roasts are, the more the user will enjoy it. Ensure that the response does not contain characters such as '*' or '`' because there is not text formatting. Also, ensure that the response is 4-5 sentences. Please use all information including, names, langauge, and stars. Github Data:\n" + githubData;
        Client geminiClient = Client.builder()
        .apiKey(geminiApiKey)
        .build();

        GenerateContentResponse aiResponse = geminiClient.models.generateContent("gemini-3.8-flash", roastPrompt, null);
        return aiResponse.text();
    }
}
