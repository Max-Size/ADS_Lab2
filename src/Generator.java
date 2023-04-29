import java.util.ArrayList;
import java.util.List;

public class Generator {
    public static List<Rectangle> generateRectangles(int amount){
        List<Rectangle> rectangles = new ArrayList<>(amount);
        for(int i=0;i<amount;i++){
            rectangles.add(new Rectangle(10L *i, 10L *i, 10L * (2L *amount-i), 10L *(2L *amount-i)));
        }
        return rectangles;
    }
    public static List<Point> generatePoint(int amount){
        List<Point> points = new ArrayList<>(amount);
        for(int i=0;i<amount;i++){
            points.add(new Point((long) (Math.pow(4723*i,31)%(20*amount)), (long) (Math.pow(4789*i,31)%(20*amount))));
        }
        return  points;
    }
}
