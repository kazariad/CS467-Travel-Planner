package edu.oregonstate.cs467.travelplanner.experience.persistence.search;

import edu.oregonstate.cs467.travelplanner.experience.model.Experience;

import java.util.List;

public class ExperienceSearchResult {
    private List<Experience> experiences;

    private int offset;

    private boolean hasNext;

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
