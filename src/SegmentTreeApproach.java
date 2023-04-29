import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SegmentTreeApproach {
    Compress compressor1 = new Compress();
    Node[] roots;
    public List<Event> events = new ArrayList<>();
    public static double log2(int x){
        return Math.log(x) / Math.log(2);
    }
    /*static void printLevelOrder(Node root) {
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node temp = queue.poll();
            System.out.print(temp+" "+temp.start_coordinate+" "+temp.end_coordinate+" val:"+temp.modifier+ " ");

            if (temp.left != null) {
                queue.add(temp.left);
            }

            if (temp.right != null) {
                queue.add(temp.right);
            }
        }
        System.out.println();
    }*/
    public void makeEvents(){
        compressor1.makeListsX_Y(Storage.rectangles);
        for(Rectangle rectangle:Storage.rectangles){
            int compressedLeftX=compressor1.getCompressedCoordinate(compressor1.listX,rectangle.leftDown.x);
            int compressedDownY=compressor1.getCompressedCoordinate(compressor1.listY,rectangle.leftDown.y);
            int compressedRightX=compressor1.getCompressedCoordinate(compressor1.listX,rectangle.rightUp.x);
            int compressedUpY=compressor1.getCompressedCoordinate(compressor1.listY,rectangle.rightUp.y);
            events.add(new Event(compressedLeftX,compressedDownY,compressedUpY-1,1));
            events.add(new Event(compressedRightX,compressedDownY,compressedUpY-1,-1));
        }
        Event.sortEvents(events);
    }
    public Node buildEmptyTree(int leftIndex,int rightIndex){
        if(log2(rightIndex+1)%1!=0){
            return buildTree(leftIndex,(int)Math.pow(2,(double)(int)log2(rightIndex)+1)-1);
        }
        return buildTree(leftIndex, rightIndex);
    }
    public Node buildTree(int leftIndex,int rightIndex){
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
    /*public void updateTree(Event event,Node node){
        if(node.start_coordinate>= event.compressedDownY && node.end_coordinate<= event.compressedUpy){
            System.out.println();
            System.out.print(node+" "+node.start_coordinate+" "+ node.end_coordinate+" val:"+node.modifier+" changing--> ");
            node.modifier+=event.type;
            System.out.println(node+" "+node.start_coordinate+" "+ node.end_coordinate+" val:"+node.modifier+" changed");
        }
        if((node.start_coordinate<= event.compressedDownY && node.end_coordinate > event.compressedUpy) ||
                (node.start_coordinate< event.compressedDownY && node.end_coordinate >= event.compressedUpy) ||
                (node.start_coordinate> event.compressedDownY && node.start_coordinate<= event.compressedUpy && node.end_coordinate> event.compressedUpy)||
                (node.start_coordinate< event.compressedDownY && node.end_coordinate>=event.compressedDownY && node.end_coordinate<event.compressedUpy)){
            //updateTree(event,node.left);
            //updateTree(event,node.right);
        }
        if((node.start_coordinate < event.compressedDownY && node.start_coordinate < event.compressedUpy) ||
                node.start_coordinate > event.compressedDownY && node.start_coordinate > event.compressedUpy){
            return;
        }
    }*/

    public Node updateTreePersistent(Event event,Node node){
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
    public void buildAllTrees(){
        roots= new Node[compressor1.listX.length];
        int eventsIndex=0;
        for(int i=0;i< roots.length;i++){
            if(i==0){
                roots[i]=buildEmptyTree(0,compressor1.listY.length-1);
            }else if(roots[i]==null){
                roots[i]=updateTreePersistent(events.get(eventsIndex),roots[i-1]);
                if(eventsIndex<events.size()-1){
                    eventsIndex++;
                }
            }
            while (events.get(eventsIndex).x==i && eventsIndex<events.size()-1){
                roots[i]=updateTreePersistent(events.get(eventsIndex),roots[i]);
                eventsIndex++;
            }
        }
    }
    public int getSumFromTree(Node node,int y){
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
    public int getAnswer(Point point){
        if(point.x<compressor1.listX[0] || point.x>compressor1.listX[compressor1.listX.length-1] ||
        point.y < compressor1.listY[0] || point.y >compressor1.listY[compressor1.listY.length-1]){
            return 0;
        }
        int compressedX = compressor1.getCompressedCoordinate(compressor1.listX, point.x);
        int compressedY = compressor1.getCompressedCoordinate(compressor1.listY, point.y);
        return getSumFromTree(roots[compressedX],compressedY);
    }
    /*public void print(){
        for(Event event: events){
            System.out.print(event.toString()+" ");
        }
    }*/
    /*public void printTrees(){
        for(Node root:roots){
            printLevelOrder(root);
            System.out.println();
        }
    }*/
}
