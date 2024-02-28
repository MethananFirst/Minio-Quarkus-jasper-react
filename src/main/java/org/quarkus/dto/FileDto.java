package org.quarkus.dto;

public class FileDto {
    private String fileName;

    private String lastModified;

    private int size;

    private String previewUrl;

    private String contentType;

    public FileDto(String fileName, String lastModified, int size, String previewUrl, String contentType) {
        this.fileName = fileName;
        this.lastModified = lastModified;
        this.size = size;
        this.previewUrl = previewUrl;
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLastModified() {
        return lastModified;
    }

    public int getSize() {
        return size;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
