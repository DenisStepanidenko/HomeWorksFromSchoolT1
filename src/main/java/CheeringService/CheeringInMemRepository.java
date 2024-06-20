package CheeringService;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CheeringInMemRepository {
    private final Map<Integer, String> phrases = new HashMap<>();

    public CheeringInMemRepository() {
        phrases.put(1, "Всё будет хорошо!");
        phrases.put(2, "Ты молодец!");
        phrases.put(3, "Ты справишься!");
    }

    public String getCheeringPhrase() {
        Random random = new Random();
        int index = random.nextInt(phrases.size()) + 1;
        return phrases.get(index);
    }

    public void addCheeringPhrase(String phrase) {
        phrases.put(phrases.size() + 1, phrase);
    }
}
