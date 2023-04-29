import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Compress {
    public long[] listX;
    public long[] listY;
    public void makeListsX_Y(List<Rectangle> list){
        Set<Long> setX = new TreeSet<>();
        Set<Long> setY = new TreeSet<>();
        for(Rectangle rectangle: list){
            setX.add(rectangle.leftDown.x);
            setX.add(rectangle.rightUp.x);
            setY.add(rectangle.leftDown.y);
            setY.add(rectangle.rightUp.y);
        }
        listX = setX.stream().mapToLong(Number::longValue).toArray();
        listY = setY.stream().mapToLong(Number::longValue).toArray();
        setX.clear();
        setY.clear();
    }
    public int getCompressedCoordinate(long[] arr,long coordinate){
        return SpecialBinarySearch.definePosition(arr,coordinate,0, arr.length-1);
    }
}
