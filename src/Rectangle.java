public class Rectangle {
    Point leftDown;
    Point rightUp;
    public Rectangle(){
        leftDown = null;
        rightUp = null;
    }
    public Rectangle(Point leftDown, Point rightUp){
        this.leftDown=leftDown;
        this.rightUp =rightUp;
    }
    public Rectangle(long leftX,long downY,long rightX,long upY){
        leftDown= new Point(leftX,downY);
        rightUp = new Point(rightX,upY);
    }
}
