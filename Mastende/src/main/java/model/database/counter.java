package model.database;

public class counter {
    private Integer count ;
    public counter(){
        this.count = 0;
    }
    public void increment() {
        count+=1;
    }
    public void reset() {
        count = 0;
    }
    public int getCount() {
        return count;
    }
}
