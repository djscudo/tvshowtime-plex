package org.nuvola.tvshowtime.business.plex;

public class Player {

    private Boolean local;
    private String publicAddress;
    private String title;
    private String uuid;

    public Boolean getLocal() {
        return local;
    }

    public void setLocal(Boolean local) {
        this.local = local;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Player{" +
                "local=" + local +
                ", publicAddress='" + publicAddress + '\'' +
                ", title='" + title + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
