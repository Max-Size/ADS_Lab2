
public class Main {

    public static void main(String[] args) {
        int amountOfRectanglesAndPoints=0;
        Storage.rectangles = Generator.generateRectangles(amountOfRectanglesAndPoints);
        Storage.points = Generator.generatePoint(amountOfRectanglesAndPoints);
        BruteForce bruteForce = new BruteForce();
        long start = System.nanoTime();
        for (Point point : Storage.points) {
            bruteForce.run(point);
        }
        long finish = System.nanoTime();
        long elapsed = finish - start;
        System.out.println("Time answering naive algorithm: "+elapsed/1000);
        start = System.nanoTime();
        CoordinateCompressionApproach coordinateCompressionApproach = new CoordinateCompressionApproach();
        coordinateCompressionApproach.buildMap();
        finish = System.nanoTime();
        elapsed = finish - start;
        System.out.println("Time preparing map algorithm: "+elapsed/1000);
        start = System.nanoTime();
        for (Point point : Storage.points) {
           coordinateCompressionApproach.run(point);
        }
        finish = System.nanoTime();
        elapsed = finish - start;
        System.out.println("Time answering map algorithm: "+elapsed/1000);
        start = System.nanoTime();
        SegmentTreeApproach segmentTreeApproach = new SegmentTreeApproach();
        segmentTreeApproach.makeEvents();
        segmentTreeApproach.buildAllTrees();
        finish = System.nanoTime();
        elapsed = finish - start;
        System.out.println("Time preparing tree algorithm: "+elapsed/1000);
        start = System.nanoTime();
        for (Point point : Storage.points) {
            segmentTreeApproach.getAnswer(point);
        }
        finish = System.nanoTime();
        elapsed = finish - start;
        System.out.println("Time answering tree algorithm: "+elapsed/1000);
    }
}
