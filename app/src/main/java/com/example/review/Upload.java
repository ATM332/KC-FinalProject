package com.example.review;

public class Upload {
    private String mName;
    private String mImageUri;
    private String mQuestions;

    public Upload() {
    }

    public Upload(String mName, String mImageUri, String mQuestions) {
        this.mName = mName;
        this.mImageUri = mImageUri;
        this.mQuestions = mQuestions;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    public String getmQuestions() {
        return mQuestions;
    }

    public void setmQuestions(String mQuestions) {
        this.mQuestions = mQuestions;
    }
}
