package de.adamson.scripts;

import de.adamson.FigureProto;
import de.adamson.JsonFigure;
import de.adamson.Settings;
import de.adamson.Utils;

import java.util.ArrayList;
import java.util.Map;

public class AddFranchise {

    public static void main(String[] args){
        addFranchise("harry-potter-24", Settings.jsonFile);
    }

    private static void addFranchise(String franchise, String fileName){
        Map<String, JsonFigure> figures = Utils.loadFiguresFromFile(fileName);

        ArrayList<FigureProto> prototypes = Utils.downloadFigurePrototypes(franchise);
        prototypes.removeIf(proto -> figures.containsKey(proto.getId()));

        JsonFigure[] newFigures = Utils.downloadFigures(prototypes).stream().map(JsonFigure::new).toArray(JsonFigure[]::new);
        for (JsonFigure figure : newFigures){
            figures.put(figure.id, figure);
        }

        if (newFigures.length > 0) {
            Utils.writeFiguresToFile(fileName, figures);
        }
    }
}
