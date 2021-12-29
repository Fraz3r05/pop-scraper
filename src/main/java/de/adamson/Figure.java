package de.adamson;

public class Figure {
    public static String unknownPic = "unknown-pic";

    private final int number;
    private final String title;
    private final String link;
    private final String photoLink;
    private final String franchise;

    public Figure(int number, String title, String link, String photoLink) {
        this.number = number;
        this.title = title;
        this.link = link;
        this.photoLink = photoLink;
        this.franchise = "franchise Lol";
    }

    public int getNumber(){
        return number;
    }

    public String getTitle(){
        return title;
    }

    public String getLink(){
        return link;
    }

    public String getId(){
        return link;
    }

    public String getPicId(){
        return photoLink;
    }

    public String getPhotoLink(){
        if (photoLink == null){
            return null;
        }

        return "https://pop-figures.com/media/img/figurine/" + photoLink + ".jpg";
    }

    public String getBoxLink(){
        if (photoLink == null){
            return null;
        }

        return "https://pop-figures.com/media/img/figurine/" + photoLink + "-box.jpg";
    }

    @Override
    public String toString() {
        return "[" + getNumber() + ", " + getTitle() + ", " + getLink() + ", " + getPhotoLink() + "]";
    }

    public String getFranchise() {
        return franchise;
    }
}
