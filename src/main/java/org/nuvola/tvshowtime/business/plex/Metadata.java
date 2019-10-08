package org.nuvola.tvshowtime.business.plex;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nuvola.tvshowtime.service.PlexService;

import java.util.Date;
import java.util.List;

public class Metadata {

    private String librarySectionType;
    private String ratingKey;
    private String key;
    private String guid;
    private String librarySectionTitle;
    private int librarySectionID;
    private String librarySectionKey;
    private String studio;
    private String type;
    private String title;
    private String originalTitle;
    private String contentRating;
    private String summary;
    private float rating;
    private long viewOffset;
    private long lastViewedAt;
    private int year;
    private String tagline;
    private String thumb;
    private String art;
    private long duration;
    private Date originallyAvailableAt;
    private long addedAt;
    private long updatedAt;
    private String primaryExtraKey;
    private String ratingImage;
    private String parentRatingKey;
    private String grandparentRatingKey;
    private String parentGuid;
    private String grandparentGuid;
    private String grandparentKey;
    private String parentKey;
    private String grandparentTitle;
    private String parentTitle;
    private String index;
    private String parentIndex;
    private String parentThumb;
    private String grandparentThumb;
    private String grandparentArt;
    private String grandparentTheme;
    private String titleSort;
    private String viewCount;
    private String chapterSource;
    @JsonProperty("Genre")
    private List<ExtraElement> genre;
    @JsonProperty("Director")
    private List<ExtraElement> director;
    @JsonProperty("Writer")
    private List<ExtraElement> writer;
    @JsonProperty("Producer")
    private List<ExtraElement> producer;
    @JsonProperty("Country")
    private List<ExtraElement> country;
    @JsonProperty("Role")
    private List<ExtraElement> role;
    @JsonProperty("Similar")
    private List<ExtraElement> similar;

    public String getGrandparentRatingKey() {
        return grandparentRatingKey;
    }

    public void setGrandparentRatingKey(String grandparentRatingKey) {
        this.grandparentRatingKey = grandparentRatingKey;
    }

    public String getParentGuid() {
        return parentGuid;
    }

    public void setParentGuid(String parentGuid) {
        this.parentGuid = parentGuid;
    }

    public String getGrandparentGuid() {
        return grandparentGuid;
    }

    public void setGrandparentGuid(String grandparentGuid) {
        this.grandparentGuid = grandparentGuid;
    }

    public String getGrandparentKey() {
        return grandparentKey;
    }

    public void setGrandparentKey(String grandparentKey) {
        this.grandparentKey = grandparentKey;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getGrandparentTitle() {
        return grandparentTitle;
    }

    public void setGrandparentTitle(String grandparentTitle) {
        this.grandparentTitle = grandparentTitle;
    }

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(String parentIndex) {
        this.parentIndex = parentIndex;
    }

    public String getGrandparentThumb() {
        return grandparentThumb;
    }

    public void setGrandparentThumb(String grandparentThumb) {
        this.grandparentThumb = grandparentThumb;
    }

    public String getGrandparentArt() {
        return grandparentArt;
    }

    public void setGrandparentArt(String grandparentArt) {
        this.grandparentArt = grandparentArt;
    }

    public String getGrandparentTheme() {
        return grandparentTheme;
    }

    public void setGrandparentTheme(String grandparentTheme) {
        this.grandparentTheme = grandparentTheme;
    }


    public String getLibrarySectionType() {
        return librarySectionType;
    }


    public String getTitleSort() {
        return titleSort;
    }

    public void setTitleSort(String titleSort) {
        this.titleSort = titleSort;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public void setLibrarySectionType(String librarySectionType) {
        this.librarySectionType = librarySectionType;
    }

    public String getRatingKey() {
        return ratingKey;
    }

    public void setRatingKey(String ratingKey) {
        this.ratingKey = ratingKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLibrarySectionTitle() {
        return librarySectionTitle;
    }

    public void setLibrarySectionTitle(String librarySectionTitle) {
        this.librarySectionTitle = librarySectionTitle;
    }

    public int getLibrarySectionID() {
        return librarySectionID;
    }

    public void setLibrarySectionID(int librarySectionID) {
        this.librarySectionID = librarySectionID;
    }

    public String getLibrarySectionKey() {
        return librarySectionKey;
    }

    public void setLibrarySectionKey(String librarySectionKey) {
        this.librarySectionKey = librarySectionKey;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getViewOffset() {
        return viewOffset;
    }

    public void setViewOffset(long viewOffset) {
        this.viewOffset = viewOffset;
    }

    public long getLastViewedAt() {
        return lastViewedAt;
    }

    public void setLastViewedAt(long lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getOriginallyAvailableAt() {
        return originallyAvailableAt;
    }

    public void setOriginallyAvailableAt(Date originallyAvailableAt) {
        this.originallyAvailableAt = originallyAvailableAt;
    }

    public long getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(long addedAt) {
        this.addedAt = addedAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPrimaryExtraKey() {
        return primaryExtraKey;
    }

    public void setPrimaryExtraKey(String primaryExtraKey) {
        this.primaryExtraKey = primaryExtraKey;
    }

    public String getRatingImage() {
        return ratingImage;
    }

    public void setRatingImage(String ratingImage) {
        this.ratingImage = ratingImage;
    }

    public List<ExtraElement> getGenre() {
        return genre;
    }

    public void setGenre(List<ExtraElement> genre) {
        this.genre = genre;
    }

    public List<ExtraElement> getDirector() {
        return director;
    }

    public void setDirector(List<ExtraElement> director) {
        this.director = director;
    }

    public List<ExtraElement> getWriter() {
        return writer;
    }

    public void setWriter(List<ExtraElement> writer) {
        this.writer = writer;
    }

    public List<ExtraElement> getProducer() {
        return producer;
    }

    public void setProducer(List<ExtraElement> producer) {
        this.producer = producer;
    }

    public List<ExtraElement> getCountry() {
        return country;
    }

    public void setCountry(List<ExtraElement> country) {
        this.country = country;
    }

    public List<ExtraElement> getRole() {
        return role;
    }

    public void setRole(List<ExtraElement> role) {
        this.role = role;
    }

    public List<ExtraElement> getSimilar() {
        return similar;
    }

    public void setSimilar(List<ExtraElement> similar) {
        this.similar = similar;
    }

    public String getParentRatingKey() {
        return parentRatingKey;
    }

    public void setParentRatingKey(String parentRatingKey) {
        this.parentRatingKey = parentRatingKey;
    }

    public String getEpisodeOrMovieName(){
        if (this.type.equalsIgnoreCase("episode")){
            return PlexService.generateEpisodeName(this);
        } else {
            return this.getTitle();
        }
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "librarySectionType='" + librarySectionType + '\'' +
                ", ratingKey='" + ratingKey + '\'' +
                ", key='" + key + '\'' +
                ", guid='" + guid + '\'' +
                ", librarySectionTitle='" + librarySectionTitle + '\'' +
                ", librarySectionID=" + librarySectionID +
                ", librarySectionKey='" + librarySectionKey + '\'' +
                ", studio='" + studio + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", contentRating='" + contentRating + '\'' +
                ", summary='" + summary + '\'' +
                ", rating=" + rating +
                ", viewOffset=" + viewOffset +
                ", lastViewedAt=" + lastViewedAt +
                ", year=" + year +
                ", tagline='" + tagline + '\'' +
                ", thumb='" + thumb + '\'' +
                ", art='" + art + '\'' +
                ", duration=" + duration +
                ", originallyAvailableAt=" + originallyAvailableAt +
                ", addedAt=" + addedAt +
                ", updatedAt=" + updatedAt +
                ", primaryExtraKey='" + primaryExtraKey + '\'' +
                ", ratingImage='" + ratingImage + '\'' +
                ", parentRatingKey='" + parentRatingKey + '\'' +
                ", grandparentRatingKey='" + grandparentRatingKey + '\'' +
                ", parentGuid='" + parentGuid + '\'' +
                ", grandparentGuid='" + grandparentGuid + '\'' +
                ", grandparentKey='" + grandparentKey + '\'' +
                ", parentKey='" + parentKey + '\'' +
                ", grandparentTitle='" + grandparentTitle + '\'' +
                ", parentTitle='" + parentTitle + '\'' +
                ", index='" + index + '\'' +
                ", parentIndex='" + parentIndex + '\'' +
                ", grandparentThumb='" + grandparentThumb + '\'' +
                ", grandparentArt='" + grandparentArt + '\'' +
                ", grandparentTheme='" + grandparentTheme + '\'' +
                ", genre=" + genre +
                ", director=" + director +
                ", writer=" + writer +
                ", producer=" + producer +
                ", country=" + country +
                ", role=" + role +
                ", similar=" + similar +
                '}';
    }

    public String getParentThumb() {
        return parentThumb;
    }

    public void setParentThumb(String parentThumb) {
        this.parentThumb = parentThumb;
    }

    public String getChapterSource() {
        return chapterSource;
    }

    public void setChapterSource(String chapterSource) {
        this.chapterSource = chapterSource;
    }
}
