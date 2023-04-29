import java.util.*;

public class ContestTree {
    public static class Node {
        Node left;
        Node right;
        int modifier;
        int start_coordinate;
        int end_coordinate;
        Node(){

        }
        Node(Node left, Node right, int modifier, int start_coordinate, int end_coordinate){
            this.left=left;
            this.right=right;
            this.modifier=modifier;
            this.start_coordinate=start_coordinate;
            this.end_coordinate=end_coordinate;
        }
        Node(Node node){
            this.left=node.left;
            this.right=node.right;
            this.modifier=node.modifier;
            this.start_coordinate=node.start_coordinate;
            this.end_coordinate= node.end_coordinate;
        }
    }
    public static class Point {
        public long x;
        public long y;
        public Point(long x,long y){
            this.x=x;
            this.y=y;
        }
    }
    public static class Rectangle {
        Point leftDown;
        Point rightUp;
        public Rectangle(long leftX,long downY,long rightX,long upY){
            leftDown= new Point(leftX, downY);
            rightUp = new Point(rightX, upY);
        }
    }
    public static class Event {
        int x;
        int compressedDownY;
        int compressedUpy;
        int type; // 1 - opening; -1 - closing
        Event(int x,int compressedDownY,int compressedUpy,int type){
            this.x=x;
            this.compressedDownY=compressedDownY;
            this.compressedUpy=compressedUpy;
            this.type=type;
        }
        public static void sortEvents(List<Event> events){
            Comparator<Event> comparator = (e1, e2) -> e1.x-e2.x;
            events.sort(comparator);
        }

        @Override
        public String toString() {
            return "{"+this.x+","+this.compressedDownY+","+this.compressedUpy+","+this.type+"}";
        }
    }
    public static int definePosition(long[] arr,long target,int leftIndex,int rightIndex){
        int midIndex = (rightIndex+leftIndex)/2;
        if(rightIndex<leftIndex){
            return midIndex;
        }
        if(target==arr[midIndex]){
            return midIndex;
        } else if (target<arr[midIndex]) {
            return definePosition(arr, target, leftIndex, midIndex-1);
        } else if (target>arr[midIndex]){
            return definePosition(arr, target, midIndex+1, rightIndex);
        }
        return -1;
    }
    public static Node buildEmptyTree(int leftIndex, int rightIndex){
        if(log2(rightIndex+1)%1!=0){
            return buildTree(leftIndex,(int)Math.pow(2,(double)(int)log2(rightIndex)+1)-1);
        }
        return buildTree(leftIndex, rightIndex);
    }
    public static Node buildTree(int leftIndex, int rightIndex){
        if(leftIndex==rightIndex){
            return new Node(null,null,0,leftIndex,rightIndex);
        }
        else{
            int midIndex=(leftIndex+rightIndex)/2;
            Node left = buildTree(leftIndex,midIndex);
            Node right = buildTree(midIndex+1,rightIndex);
            return new Node(left,right,0,leftIndex,rightIndex);
        }
    }
    public static Node updateTreePersistent(Event event, Node node){
        if(node.start_coordinate>= event.compressedDownY && node.end_coordinate<= event.compressedUpy){
            node = new Node(node);
            node.modifier+=event.type;
            return node;
        }else
        if((node.start_coordinate<= event.compressedDownY && node.end_coordinate > event.compressedUpy) ||
                (node.start_coordinate< event.compressedDownY && node.end_coordinate >= event.compressedUpy) ||
                (node.start_coordinate> event.compressedDownY && node.start_coordinate<= event.compressedUpy && node.end_coordinate> event.compressedUpy)||
                (node.start_coordinate< event.compressedDownY && node.end_coordinate>=event.compressedDownY && node.end_coordinate<event.compressedUpy)){
            node = new Node(node);
            node.left = updateTreePersistent(event,node.left);
            node.right = updateTreePersistent(event,node.right);
            return node;
        }
        return node;
    }
    public static void buildAllTrees(Node[] roots,long[] listY,List<Event> events){
        int eventsIndex=0;
        for(int i=0;i< roots.length;i++){
            if(i==0){
                roots[i]=buildEmptyTree(0,listY.length-1);
            }else if(roots[i]==null){
                roots[i]=updateTreePersistent(events.get(eventsIndex),roots[i-1]);
                if(eventsIndex<events.size()-1){
                    eventsIndex++;
                }
            }
            while (events.get(eventsIndex).x==i && eventsIndex<events.size()-1){
                roots[i]=updateTreePersistent(events.get(eventsIndex++),roots[i]);
            }
        }
    }
    public static int getSumFromTree(Node node, int y){
        if(y==node.end_coordinate && y==node.start_coordinate){
            return node.modifier;
        }
        int midVal = (node.start_coordinate+node.end_coordinate)/2;
        if(y>midVal){
            return getSumFromTree(node.right,y)+ node.modifier;
        }else{
            return getSumFromTree(node.left,y)+ node.modifier;
        }
    }
    public static int getAnswer(Point point,Node[] roots,long[] listX,long[] listY){
        if(point.x<listX[0] || point.x>listX[listX.length-1] ||
                point.y < listY[0] || point.y >listY[listY.length-1]){
            return 0;
        }
        int compressedX = getCompressedCoordinate(listX, point.x);
        int compressedY = getCompressedCoordinate(listY, point.y);
        return getSumFromTree(roots[compressedX],compressedY);
    }
    public static int getCompressedCoordinate(long[] arr,long coordinate){
        return definePosition(arr,coordinate,0, arr.length-1);
    }
    public static double log2(int x){
        return Math.log(x) / Math.log(2);
    }
    public static void main(String[] args) {
        List<Rectangle> rectangles = new ArrayList<>();
        List<Point> points = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        Node[] roots;
        long[] listX;
        long[] listY;
        Scanner in = new Scanner(System.in);
        int rectanglesAmount = in.nextInt();
        for(int i=0;i<rectanglesAmount;i++){
            rectangles.add(new Rectangle(in.nextLong(), in.nextLong(), in.nextLong(), in.nextLong()));
        }
        int pointsAmount = in.nextInt();
        for(int i=0;i<pointsAmount;i++){
            points.add(new Point(in.nextLong(), in.nextLong()));
        }
        Set<Long> setX = new TreeSet<>();
        Set<Long> setY = new TreeSet<>();
        for(Rectangle rectangle: rectangles){
            setX.add(rectangle.leftDown.x);
            setX.add(rectangle.rightUp.x);
            setY.add(rectangle.leftDown.y);
            setY.add(rectangle.rightUp.y);
        }
        listX = setX.stream().mapToLong(Number::longValue).toArray();
        listY = setY.stream().mapToLong(Number::longValue).toArray();
        setX.clear();
        setY.clear();
        for(Rectangle rectangle:rectangles){
            int compressedLeftX=getCompressedCoordinate(listX,rectangle.leftDown.x);
            int compressedDownY=getCompressedCoordinate(listY,rectangle.leftDown.y);
            int compressedRightX=getCompressedCoordinate(listX,rectangle.rightUp.x);
            int compressedUpY=getCompressedCoordinate(listY,rectangle.rightUp.y);
            events.add(new Event(compressedLeftX,compressedDownY,compressedUpY-1,1));
            events.add(new Event(compressedRightX,compressedDownY,compressedUpY-1,-1));
        }
        Event.sortEvents(events);
        roots= new Node[listX.length];
        buildAllTrees(roots,listY,events);
        int iter=0;
        for (Point point : points) {
            if(points.size()==1){
                System.out.print(getAnswer(point,roots,listX,listY));
            }else
            if (iter == 0) {
                System.out.print(getAnswer(point,roots,listX,listY));
            }
            else{
                System.out.print(" "+getAnswer(point,roots,listX,listY));
            }
            iter++;
        }
    }
}
