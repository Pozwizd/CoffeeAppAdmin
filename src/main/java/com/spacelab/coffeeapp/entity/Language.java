package com.spacelab.coffeeapp.entity;

import lombok.Getter;

@Getter
public enum Language {
    EN("English"),
    RU("Русский");

    Language(String languageName) {
        this.languageName = languageName;
    }

    private String languageName;

}