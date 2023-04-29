public class Node {
    Node left;
    Node right;
    int modifier;
    int start_coordinate;
    int end_coordinate;
    Node(){

    }
    Node(Node left,Node right,int modifier,int start_coordinate,int end_coordinate){
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
