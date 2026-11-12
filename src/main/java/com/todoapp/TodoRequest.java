package com.todoapp;

public record TodoRequest(String text, Boolean completed) {
}