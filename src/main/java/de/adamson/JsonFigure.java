package de.adamson;

public class JsonFigure {
    public String title;
    public int number;
    public String id;
    public String picId;
    public String collection;
    public String franchise;

    public JsonFigure(String title, int number, String id, String picId, String collection, String franchise) {
        this.title = title;
        this.number = number;
        this.id = id;
        this.picId = picId;
        this.collection = collection;
        this.franchise = franchise;
    }

    public JsonFigure(Figure figure){
        this(figure.getTitle(), figure.getNumber(), figure.getId(), figure.getPicId() == null ? Figure.unknownPic : figure.getPicId(), "", figure.getFranchise());
    }

    @Override
    public String toString() {
        return "[" + title + ", " + number + ", " + id + ", " + picId + ", " + collection + ", " + franchise + "]";
    }
}
