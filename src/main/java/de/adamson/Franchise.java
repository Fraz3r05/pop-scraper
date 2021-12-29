package de.adamson;

public class Franchise {
    private final String title;
    private final String link;

    public Franchise(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle(){
        return title;
    }

    public String getLink(){
        return link;
    }

    @Override
    public String toString() {
        return "[" + getTitle() + ", " + getLink() + "]";
    }
}
