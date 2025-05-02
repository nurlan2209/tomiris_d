package com.diploma.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "message")
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(optional = false)
    private User user;

    @Column(name = "user_message", length = 0)
    private String userMessage;

    @Column(name = "bot_message", length = 0)
    private String botMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getBotMessage() {
        return botMessage;
    }

    public void setBotMessage(String botMessage) {
        this.botMessage = botMessage;
    }
}
