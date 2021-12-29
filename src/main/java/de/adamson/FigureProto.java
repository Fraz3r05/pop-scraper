package de.adamson;

public class FigureProto {
    private final int number;
    private final String title;
    private final String id;

    public FigureProto(int number, String title, String id) {
        this.number = number;
        this.title = title;
        this.id = id;
    }

    public int getNumber(){
        return number;
    }

    public String getTitle(){
        return title;
    }

    public String getId(){
        return id;
    }

    @Override
    public String toString() {
        return "[" + getNumber() + ", " + getTitle() + ", " + getId() + "]";
    }
}
