package com.example.myapplication.viewmodels;

public class BankItem {
    private String word;
    private boolean isActive;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public BankItem(String word) {
        this.word = word;
        isActive = true;
    }

    public BankItem(String word, boolean isActive) {
        this.word = word;
        this.isActive = isActive;
    }
}
