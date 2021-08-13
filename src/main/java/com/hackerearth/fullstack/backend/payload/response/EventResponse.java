package com.hackerearth.fullstack.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventResponse {
    public int id;
    public String type;
    public int repoId;
    public int actorId;
    @JsonProperty("public")
    public boolean isPublic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRepoId() {
        return repoId;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public boolean equals(Object obj) {
        EventResponse et=(EventResponse) obj;
        return et.getActorId()==getActorId()&&
                et.getType().equals(getType())&&
                et.getRepoId()==et.getRepoId()&&
                et.isPublic()==isPublic();
    }
}
