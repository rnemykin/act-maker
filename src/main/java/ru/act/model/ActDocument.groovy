package ru.act.model

class ActDocument {
    
    byte[] bytes
    String fileName

    ActDocument(byte[] bytes, String fileName) {
        this.bytes = bytes
        this.fileName = fileName
    }

}
