# ADS_Lab2
The main goal of this laboratory work is to produce three algorithms below which provides the solution and compare their time complexity for both preparing and giving answer:
- Naive approach
- Building map (including coordinates compression)
- Segment tree (including coordinates compression)
## Complexities
|    | Naive | Map | Tree |
|----|-------|-----|------|
|Preparing complexity| O(1) | O(N^3) | O(NlogN)|
|Answering complexity| O(N^2)| O(logN) | O(logN)|
## Classes
Of course we should define some new classes to easier parse data and work with 
### Point
First one is a point. It's a class describing two point `x` and `y` both of them is `long` type due to input wavering `0<=10^9` so the `int` can't handle with that range
```java
public class Point {
    public long x;
    public long y;
    public Point(){
        x= Long.parseLong(null);
        y= Long.parseLong(null);
    }
    public Point(long x,long y){
        this.x=x;
        this.y=y;
    }
}
```
### Rectangle
Next one is a rectangle, it is a class which consists of two `Points`, first one is a left down coordinate and the second is right up coordinate
```java
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
```

