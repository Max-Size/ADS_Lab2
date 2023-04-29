
public class BruteForce {
    public int run(Point point){
        int amountOfCoveredRectangles=0;
        long start = System.nanoTime();
        for(Rectangle rectangle: Storage.rectangles){
            if(point.x >= rectangle.leftDown.x && point.x<rectangle.rightUp.x &&
            point.y >= rectangle.leftDown.y && point.y <rectangle.rightUp.y) amountOfCoveredRectangles++;
        }
        return amountOfCoveredRectangles;
    }
}
