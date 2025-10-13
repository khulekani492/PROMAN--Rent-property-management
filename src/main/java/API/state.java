package API;
/**
 * This class is responsible for maintaining the state of room occupancy
 * to prevent inserting duplicate room numbers for different tenants.
 *
 * <p>While a database-level UNIQUE constraint could normally prevent this issue,
 * that approach is not viable in this context since multiple landlords
 * may have rooms with the same room number (e.g., "Room 1" in different properties).</p>
 *
 * <p>To handle this, the system uses a global state tracker that records the
 * currently occupied rooms per property. Whenever a new tenant is added,
 * the API checks whether the specified room number is already occupied
 * within that landlord's property. If it is, the operation is rejected
 * with an appropriate error message indicating that the room is already taken.</p>
 *
 * <p>This ensures logical consistency and prevents overlapping room assignments
 * without enforcing a global UNIQUE constraint at the database level.</p>
 */

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
