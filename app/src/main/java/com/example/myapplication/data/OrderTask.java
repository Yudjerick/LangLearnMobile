package com.example.myapplication.data;

import androidx.room.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Entity
public class OrderTask {
    private String translatedPhrase;
    private final ArrayList<String> answer;
    private ArrayList<String> additionalVariants;

    public OrderTask(String translatedPhrase, String[] answer, String[] additionalVariants) {
        this.translatedPhrase = translatedPhrase;
        this.answer = new ArrayList<>(Arrays.asList(answer));
        this.additionalVariants = new ArrayList<>(Arrays.asList(additionalVariants));
    }

    public OrderTask(String translatedPhrase, String[] answer) {
        this.translatedPhrase = translatedPhrase;
        this.answer = new ArrayList<>(Arrays.asList(answer));
        this.additionalVariants = new ArrayList<>();
    }
    public String getTranslatedPhrase() {
        return translatedPhrase;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }



    public List<String> getVariants() {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(answer);
        list.addAll(additionalVariants);
        return list;
    }

    public boolean checkAnswer(List<String> givenAnswer){
        return answer.equals(givenAnswer);
    }
}
