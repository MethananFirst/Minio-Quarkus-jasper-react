package org.quarkus.dto;

public class LetterDto {
    private String senderAddress;
    private String date;
    private String recipientName;
    private String recipientAddress;
    private String salutation;
    private String content1;
    private String content2;
    private String content3;
    private String closing;
    private String signature;

    public LetterDto(String senderAddress, String date, String recipientName, String recipientAddress, String salutation, String content1, String content2, String content3, String closing, String signature) {
        this.senderAddress = senderAddress;
        this.date = date;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
        this.salutation = salutation;
        this.content1 = content1;
        this.content2 = content2;
        this.content3 = content3;
        this.closing = closing;
        this.signature = signature;
    }

    public LetterDto() {

    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }
}
