package de.adamson;

public class JsonFigureArray {
    public String title;
    public int number;
    public String id;
    public String collection;
    public String franchise;

    public JsonFigureArray(String title, int number, String id, String collection, String franchise) {
        this.title = title;
        this.number = number;
        this.id = id;
        this.collection = collection;
        this.franchise = franchise;
    }

    public JsonFigureArray(Figure figure){
        this(figure.getTitle(), figure.getNumber(), figure.getId() == null ? "unknown-pop" : figure.getId(), "", figure.getFranchise());
    }
}
