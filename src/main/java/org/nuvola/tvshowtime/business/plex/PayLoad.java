package org.nuvola.tvshowtime.business.plex;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayLoad {

    private String event;
    private Boolean user;
    private Boolean owner;
    @JsonProperty("Account")
    private Account account;
    @JsonProperty("Server")
    private Server server;
    @JsonProperty("Player")
    private Player player;
    @JsonProperty("Metadata")
    private Metadata metadata;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Boolean getUser() {
        return user;
    }

    public void setUser(Boolean user) {
        this.user = user;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "PayLoad{" +
                "event='" + event + '\'' +
                ", user=" + user +
                ", owner=" + owner +
                ", account=" + account +
                ", server=" + server +
                ", player=" + player +
                ", metadata=" + metadata +
                '}';
    }
}
