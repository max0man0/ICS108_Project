import java.io.Serializable;

@SuppressWarnings("serial")
public class Question implements Serializable {
    private String questionText;
    private String[] choices;
    private int correctChoiceIndex;
    
    public Question(String questionText, String[] choices, int correctChoiceIndex) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctChoiceIndex = correctChoiceIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public int getCorrectChoiceIndex() {
        return correctChoiceIndex;
    }

    public void setCorrectChoiceIndex(int correctChoiceIndex) {
        this.correctChoiceIndex = correctChoiceIndex;
    }
    
    @Override
    public String toString() {
    	return questionText;
    }
}