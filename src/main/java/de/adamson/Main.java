package de.adamson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //https://funko.fandom.com/wiki/Pop!_Harry_Potter

        String franchise = "harry-potter-24";

        String dir = "D:/Users/Yanik/Desktop/funko/";

        JsonFigure[] newFigures = Utils.downloadFigures(franchise).stream().map(JsonFigure::new).toArray(JsonFigure[]::new);

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();

        //writeToFile(gson.toJson(newFigures), "pop.json");

        Map<String, JsonFigure> oldFigures = Utils.loadFiguresFromFile("pop.json");

        System.out.println(Arrays.toString(oldFigures.values().toArray(new JsonFigure[0])));
        //downloadFranchiseToDir(franchise, dir);
    }


}
