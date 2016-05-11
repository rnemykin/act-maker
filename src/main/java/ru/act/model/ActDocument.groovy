package ru.act.model

class ActDocument {
    private byte[] bytes;
    private String fileName;

    ActDocument(byte[] bytes, String fileName) {
        this.bytes = bytes
        this.fileName = fileName
    }

    byte[] getBytes() {
        return bytes
    }

    String getFileName() {
        return fileName
    }
}
