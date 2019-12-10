package com.fengchao.order.model;

public class ImsMcMembersWithBLOBs extends ImsMcMembers {
    private String bio;

    private String interest;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio == null ? null : bio.trim();
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest == null ? null : interest.trim();
    }
}