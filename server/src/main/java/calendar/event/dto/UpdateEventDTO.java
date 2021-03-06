package calendar.event.dto;

import java.util.List;

/**
 * Class UpdateEventDTO
 *
 * @author Leif Karlsson (leifkarlsson)
 */
public class UpdateEventDTO {

    private String id;
    private String name;
    private String location;
    private String description;
    private long startDateTime;
    private long duration;
    private boolean recurring;
    private String recursEvery;
    private long recursUntil;
    private List<String> images;
    private long createdAt;
    private long updatedAt;
    private String createdBy;
    private String editedBy;
    private String path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(long startDateTime) {
        this.startDateTime = startDateTime;
    }
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getRecursEvery() {
        return recursEvery;
    }

    public void setRecursEvery(String recursEvery) {
        this.recursEvery = recursEvery;
    }

    public long getRecursUntil() {
        return recursUntil;
    }

    public void setRecursUntil(long recursUntil) {
        this.recursUntil = recursUntil;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
