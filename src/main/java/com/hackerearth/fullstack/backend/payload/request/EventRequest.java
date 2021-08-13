package com.hackerearth.fullstack.backend.payload.request;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventRequest {
    private String type;
    @JsonProperty("public")
    private boolean isPublic;
    private long repoId;
    private long actorId;

    public EventRequest() {
    }

    public EventRequest(String type, boolean isPublic, long repoId, long actorId) {
        this.type = type;
        this.isPublic = isPublic;
        this.repoId = repoId;
        this.actorId = actorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public long getRepoId() {
        return repoId;
    }

    public void setRepoId(long repoId) {
        this.repoId = repoId;
    }

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }
}
