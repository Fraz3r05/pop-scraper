package de.adamson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Utils {

    public static void writeToFile(String content, String fileName) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(content);
            writer.close();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public static Map<String, JsonFigure> loadFiguresFromFile(String fileName) {
        Map<String, JsonFigure> figureMap = new HashMap<>();

        String fileContent = readFile(fileName);
        JsonFigure[] fileFigures = new GsonBuilder().create().fromJson(fileContent, JsonFigure[].class);

        for (JsonFigure figure : fileFigures){
            figureMap.put(figure.id, figure);
        }

        return figureMap;
    }

    public static void writeFiguresToFile(String fileName, Map<String, JsonFigure> figureMap) {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();

        writeToFile(gson.toJson(figureMap.values().toArray(new JsonFigure[0])), fileName);
    }

    public static String readFile(String fileName) {
        try{
            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String line = reader.readLine();
            while(line != null){
                result.append(line + '\n');
                line = reader.readLine();
            }

            reader.close();

            return result.toString();
        } catch (IOException exception){
            exception.printStackTrace();
        }

        return null;
    }

    public static ArrayList<String> getContentBetween(String prefix, String suffix, String content, int maxContentLength){
        ArrayList<String> list = new ArrayList<>();
        String template = prefix;

        int index = content.indexOf(template);
        while (index > 0){
            String namePlusLength = content.substring(index + template.length(), index + template.length() + maxContentLength);
            String name = namePlusLength.substring(0, namePlusLength.indexOf(suffix)).trim();
            list.add(name);

            index = content.indexOf(template, index+1);
        }

        return list;
    }

    public static String makeHtmlRequest(String url) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return content.toString();
        } catch (IOException e){
            e.printStackTrace();
        }

        throw new NullPointerException();
    }

    public static ArrayList<Franchise> getFranchises(){
        String franchisesHtml = makeHtmlRequest("https://pop-figures.com/franchises");

        ArrayList<String> titles = getContentBetween("<p itemprop=\"name\" class=\"card-text text-center\">", "</p>", franchisesHtml, 100);
        ArrayList<String> links = getContentBetween("<a itemprop=\"url\" href=\"//pop-figures.com/franchise/", "\" title=\"", franchisesHtml, 100);

        ArrayList<Franchise> franchises = new ArrayList<>();

        for (int i = 0; i < titles.size(); i++){
            franchises.add(new Franchise(titles.get(i), links.get(i)));
        }

        return franchises;
    }

    public static ArrayList<FigureProto> downloadFigurePrototypes(String franchise){
        String franchisesHtml = makeHtmlRequest("https://pop-figures.com/franchise/" + franchise);

        ArrayList<String> figuresHtml = getContentBetween("/>\t\t\t\t\t\t\t\t<a href=\"//pop-figures.com/figure/funko-pop-", "\">", franchisesHtml, 500);
        System.out.println(figuresHtml.size());

        ArrayList<FigureProto> figures = new ArrayList<>();

        for (int i = 0; i < figuresHtml.size(); i++){
            String figureHtml = figuresHtml.get(i).replace("\" class=\"unstyled", "");

            int number = Integer.parseInt(figureHtml.substring(0, figureHtml.indexOf("-")));
            String link = figureHtml.substring(0, figureHtml.indexOf("\" title="));
            String title = figureHtml.substring(figureHtml.indexOf("\" title=") + "\" title=".length() + 1);

            figures.add(new FigureProto(number, title, link));

            System.out.println("FigurePrototypes: " + (i+1) + "/" + figuresHtml.size() + "    " + figures.get(i).getTitle());
        }

        return figures;
    }

    public static Figure downloadFigure(String figure){
        String figureHtml = makeHtmlRequest("https://pop-figures.com/figure/" + figure);
        String title = getContentBetween("<h2 itemprop=\"name\">POP action figure of ", "<", figureHtml, 100).get(0);

        String numberTemplate = "<span class=\"bage rounded-pill bg-light\">#";
        int number = 0;
        if (figureHtml.contains(numberTemplate)){
            number = Integer.parseInt(getContentBetween(numberTemplate, "<", figureHtml, 100).get(0));
        }


        String photoTemplate = "<img src=\"//pop-figures.com/media/img/figurine/";
        String photoTemplateFallback = "<img itemprop=\"image\" src=\"//pop-figures.com/media/img/figurine/";
        String photoLink = null;
        if (figureHtml.contains(photoTemplate)){
            photoLink = getContentBetween(photoTemplate, ".jpg\"", figureHtml, 100).get(0);
        } else if (figureHtml.contains(photoTemplateFallback)) {
            photoLink = getContentBetween(photoTemplateFallback, ".jpg\"", figureHtml, 100).get(0);
        }

        return new Figure(number, title, figure, photoLink);
    }

    public static ArrayList<Figure> downloadFigures(String franchise){
        return downloadFigures(downloadFigurePrototypes(franchise));
    }

    public static ArrayList<Figure> downloadFigures(ArrayList<FigureProto> prototypes){
        ArrayList<Figure> figures = new ArrayList<>();

        for (int i = 0; i < prototypes.size(); i++){
            figures.add(downloadFigure(prototypes.get(i).getId()));

            System.out.println("Figures: " + (i+1) + "/" + prototypes.size() + "    " + figures.get(i).getTitle());
        }

        return figures;
    }

    public static void downloadPhotosToDir(JsonFigure[] figures, String dir){
        for (int i = 0; i < figures.length; i++){
            downloadFigurePhotosToDir(figures[i], dir);
            System.out.println("FigurePhoto: " + (i+1) + "/" + figures.length + "    " + figures[i].title);
        }
    }

    public static void downloadFigurePhotosToDir(JsonFigure figure, String dir){
        if (!figure.picId.equals(Figure.unknownPic)){
            writeImage(downloadImage("https://pop-figures.com/media/img/figurine/" + figure.picId + ".jpg", figure.id), dir + figure.id + ".jpg");
            writeImage(downloadImage("https://pop-figures.com/media/img/figurine/" + figure.picId + "-box.jpg", figure.id + " BOX"), dir + figure.id + "-box.jpg");
        } else {
            System.err.println("Unknown pic: " + figure.id);
        }
    }

    public static Image downloadImage(String url){
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            System.err.println(e.getMessage() + ": " + url);
        }

        return null;
    }

    public static Image downloadImage(String url, String id){
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            System.err.println(e.getMessage() + ": " + id);
        }

        return null;
    }

    public static void writeImage(Image image, String fileName){
        if (image == null){
            return;
        }

        try {
            ImageIO.write((RenderedImage) image, "jpg", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> listFilesInDir(String dir) {
        ArrayList<String> files = new ArrayList<>();

        for (File fileEntry : new File(dir).listFiles()) {
            if (fileEntry.getName().endsWith(".jpg")) {
                files.add(fileEntry.getName().replace(".jpg", ""));
            }
        }

        return files;
    }
}
