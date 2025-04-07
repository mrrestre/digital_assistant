package com.code.interview.digital_assistant.model;

import jakarta.persistence.*;

@Entity
public class DigitalAssistant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String assistantName;

    private String assistantMessage;


    // Getter and Setters

    public String getAssistantName() {
        return assistantName;
    }

    public String getAssistantMessage() {
        return assistantMessage;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public void setAssistantMessage(String assistantMessage) {
        this.assistantMessage = assistantMessage;
    }
}
