package API;

public class state {
    private Integer current_track;

    public  state(Integer current_track){
        this.current_track = current_track;
    }

    public Integer getCurrent_track(){
        return  this.current_track;
    }

    public void setCurrent_track(Integer current_track){
        this.current_track =  current_track;

    }
}
