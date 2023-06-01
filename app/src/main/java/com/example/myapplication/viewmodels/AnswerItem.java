package com.example.myapplication.viewmodels;

public class AnswerItem {
    private String word;
    private BankItem associated;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public BankItem getAssociated() {
        return associated;
    }

    public void setAssociated(BankItem associated) {
        this.associated = associated;
    }

    public AnswerItem(String word, BankItem associated) {
        this.word = word;
        this.associated = associated;
    }
}
