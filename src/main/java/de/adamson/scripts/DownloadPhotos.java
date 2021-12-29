package de.adamson.scripts;

import de.adamson.Figure;
import de.adamson.JsonFigure;
import de.adamson.Settings;
import de.adamson.Utils;

import java.util.ArrayList;
import java.util.Collection;

public abstract class DownloadPhotos {

    public static void main(String[] args){
        downloadPhotos(Settings.jsonFile, Settings.imagesDir);
    }

    private static void downloadPhotos(String fileName, String dir){
        ArrayList<String> alreadyDownloadedImages = Utils.listFilesInDir(dir);

        Collection<JsonFigure> figures = Utils.loadFiguresFromFile(fileName).values();
        figures.removeIf(figure -> (alreadyDownloadedImages.contains(figure.id) && alreadyDownloadedImages.contains(figure.id + "-box")) || figure.picId.equals(Figure.unknownPic));

        Utils.downloadPhotosToDir(figures.toArray(new JsonFigure[0]), dir);
    }

}
