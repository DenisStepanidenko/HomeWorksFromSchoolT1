package CheeringService;

public class CheeringManager {
    private final CheeringInMemRepository cheeringInMemRepository;

    public CheeringManager(){
        cheeringInMemRepository = new CheeringInMemRepository();
    }

    public String getPhrase(){
         return cheeringInMemRepository.getCheeringPhrase();
    }

    public String addCheeringPhrase(String phrase){
        cheeringInMemRepository.addCheeringPhrase(phrase);

        return "Добавлена фраза " + phrase;
    }
}
