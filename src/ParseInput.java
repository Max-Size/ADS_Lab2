import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ParseInput {
    public static void readRectangles(String rectangleString){
        rectangleString = rectangleString.replaceAll("\\(","");
        rectangleString = rectangleString.replaceAll("\\)","");
        rectangleString = rectangleString.replaceAll("\\{","");
        rectangleString = rectangleString.replaceAll("}","");
        rectangleString = rectangleString.replaceAll(" ","");
        int[] rectanglePoints = Arrays.stream(rectangleString.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        for(int i=0;i<rectanglePoints.length;i+=4){
            Storage.rectangles.add(new Rectangle(rectanglePoints[i],
                    rectanglePoints[i+1],rectanglePoints[i+2],rectanglePoints[i+3]));
        }
    }
    public static void readInput(List<Rectangle> rectangles, List<Point> points){
        Scanner in = new Scanner(System.in);
        int rectanglesAmount = in.nextInt();
        for(int i=0;i<rectanglesAmount;i++){
            rectangles.add(new Rectangle(in.nextLong(),in.nextLong(),in.nextLong(),in.nextLong()));
        }
        int pointsAmount = in.nextInt();
        for(int i=0;i<pointsAmount;i++){
            points.add(new Point(in.nextLong(),in.nextLong()));
        }
    }
}
