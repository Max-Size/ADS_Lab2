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
 Сначала необходимо определить вспомогательные классы `Event` - для левой и правой стороны квадрата, который будет отражать диапозон по `y` открытия и закрытия прямоугольника и `Node` для узлов дерева
 ```java
 import java.util.Comparator;
import java.util.List;

public class Event {
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
}
```
```java
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
```
Первым шагом необходимо создать список `events` и отсортровать их по `x`, чтобы было удобнее пробегаться по ним при построении деревьев
```java
Compress compressor1 = new Compress();
public List<Event> events = new ArrayList<>();
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
```
Для сжатия используем метод объекта класса `Compressor` как и во втором алгоритме  
Далее по этим `Event` будем строить деревья, сначала я строю пустое дерево с количеством листьев равным степени 2, для удобства при построении дерева
```java
public static double log2(int x){
    return Math.log(x) / Math.log(2);
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
```
После этого строю персистетно для каждого `x` свою версию дерева
```java
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
```
И в конце концов используя все выше перечисленные методы, строю все деревья, где каждый `root[i]` хранит свою версию дерева для конкретного `x`
```java
Node[] roots;
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
```
Чтобы получить ответ по этим деревьям, сначала необходимо найти свою версию по `x`, а затем пройти от корня прибавляя к ответу значения модфикаторов вплоть до листа, который определяется в зависимости `y` конкретной точки
```java
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
```
## Тестирование
Для генерации набора прямоугольников и точек запросы использовались рекомендации указанные к работе
```java
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
```
## Графики
Сырые данные для графиков представлены в [таблице](https://github.com/Max-Size/ADS_Lab2/blob/main/Lab_2.xlsx)
Для тестирования использовалось совпадающее количество прямоугольников и точек во всех случаях  
Результаты по времени подготовки

![Preparing_res](https://user-images.githubusercontent.com/125485047/235325632-de4c781b-5fb5-44f4-a5b5-ca3bcab7ddeb.png)

Результаты по времени получения ответа

![Answering_res](https://user-images.githubusercontent.com/125485047/235325697-f5f289d2-5d95-42ed-9920-75c6efc4529b.png)

## Выводы
Судя по результатам тестов, можно сказать, что алгоритм на карте будет малоэффективен на большом колчиестве прямоугольников, так как на построение карты уходит слишком много времени `O(N^3)`. Однако на большом количестве запросов - карта является самым эффективным инструментом, так как по факту чтобы получить ответ на запрос этот алгоритм тратит не более `2logN` операций, в то время как его главный конкурент - алгоритм на дереве тратит слегка больше времени на получение ответа: `3logN`, так как ему еще приходится потратить `logN` операций на поиск ответа в дереве. Но зато алгоритм на дереве является более эффективным в среднем, так как на подготовку данных он тратит `O(NlogN)` времени, что очевидно лучше кубической сложности. Также наивный алгоритм хорош, если не требуется отвечать на большое количество запросов, ведь на поиск ответа он тратит `O(N^2)`, на зато совсем не требует времени на подготовку, что является достаточно весомым плюсом, но разве что в случае с небольшим количеством запросов.
