
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
