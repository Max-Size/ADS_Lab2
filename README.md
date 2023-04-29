# АиСД_Лаб2
Основная цель этой лабораторной работы - создать приведенные ниже алгоритмы, которые обеспечивают решение, и сравнить их временную сложность как для подготовки, так и для предоставления ответа:
- Наивный подход
- Карта прямоуголькниов (включая сжатие координат)
- Дерево отрезков (включая сжатие координат)
## Сложности
|    | Naive | Map | Tree |
|----|-------|-----|------|
|Preparing complexity| O(1) | O(N^3) | O(NlogN)|
|Answering complexity| O(N^2)| O(logN) | O(logN)|
## Базовые классы 
### Точка
Первый из них - это точка. Это класс, описывающий две точки `x` и `y`. Обе они имеют тип `long` из-за колебаний входных данных `0< = 10 ^ 9`, так как `int` не может работать с этим диапазоном
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
### Прямоугольник
Следующий - прямоугольник, это класс, который состоит из двух `Point`, первая из которых представляет собой левую нижнюю координату, а вторая - правую верхнюю координату
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
## Алгоритмы
### Наивный алгоритм
Итак, идея первого алгоритма состоит в том, чтобы просто проверить, принадлежит ли текущая `Point` какому-либо `Rectangle`, сравнивая координаты `x` и `y` точки запроса с координатами `Leftdown` и `rightUp` каждого прямоугольника
```java
public class BruteForce {
    public int run(Point point){
        int amountOfCoveredRectangles=0;
        for(Rectangle rectangle: Storage.rectangles){
            if(point.x >= rectangle.leftDown.x && point.x<rectangle.rightUp.x &&
            point.y >= rectangle.leftDown.y && point.y <rectangle.rightUp.y) amountOfCoveredRectangles++;
        }
        return amountOfCoveredRectangles;
    }
}
```
### Карта прямоугольников
В случае с большими значениями координат с сильным разбросом нецелоосбразно строить карту, так как может оказаться слишком болших размеров, поэтому сначала координаты надо сжать, для этого я использую бинарный поиск и вспомогательный класс `Compress` на основе которого и происходит сжатие координат:  
Бинарный поиск:
```java
public class SpecialBinarySearch {
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
}
```  
Класс `Compress`, в нем строятся отсортированные по неубыванию списки уникальных значений `x` и `y` по которым и происходит дальнейшее сжатие с помощью бинарного поиска:
```java
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
```  
Сама карта строится с помощью нахождения новых сжатых точек прямоугольника и увеличения текущих значений в соответсвующей матрице на 1
```java
int[][] map;
Compress compressor = new Compress();
    public void buildMap(){
        compressor.makeListsX_Y(Storage.rectangles);
        map = new int[compressor.listX.length][compressor.listY.length];
        for(Rectangle rectangle: Storage.rectangles){
            int compressedLeftX=compressor.getCompressedCoordinate(compressor.listX,rectangle.leftDown.x);
            int compressedDownY= compressor.getCompressedCoordinate(compressor.listY,rectangle.leftDown.y);
            int compressedRightX= compressor.getCompressedCoordinate(compressor.listX,rectangle.rightUp.x);
            int compressedUpY= compressor.getCompressedCoordinate(compressor.listY,rectangle.rightUp.y);
            for(int i=compressedLeftX;i<compressedRightX;i++){
                for(int j=compressedDownY;j<compressedUpY;j++){
                    map[i][j]++;
                }
            }
        }
    }
 ```
 Соответсвенно чтобы найти ответ по запрошенной точке, сначала необходимо сжать ее кординаты по уже упомянутой процедуре и просто взять соответсвующее значение из `map`
 ```java
 public int run(Point point){
        if(point.x<compressor.listX[0] || point.x>compressor.listX[compressor.listX.length-1] ||
                point.y < compressor.listY[0] || point.y >compressor.listY[compressor.listY.length-1]){
            return 0;
        }
        int compressedX = compressor.getCompressedCoordinate(compressor.listX, point.x);
        int compressedY = compressor.getCompressedCoordinate(compressor.listY, point.y);
        return map[compressedX][compressedY];
    }
 ```
 ### Дерево отрезков
 Для работы этого алгоритма необходимо создать персистентное дерево отрезков, где каждая версия этого дерева - столбец  
 Для боковых
