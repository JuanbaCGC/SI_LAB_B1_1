package LaberintoGit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Celda {
	 	int x;
	    int y;
	    
	    boolean norte,sur,oeste,este;
	   
	    public Celda(int x, int y) {
	        this.x = x;
	        this.y = y;
	        norte = false;
	        sur = false;
	        oeste = false;
	        este = false;
	    }

	    @Override
	    public int hashCode() {
	        int hash = 7;
	        hash = 53 * hash + this.x;
	        hash = 53 * hash + this.y;
	        return hash;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj) {
	            return true;
	        }
	        if (obj == null) {
	            return false;
	        }
	        if (getClass() != obj.getClass()) {
	            return false;
	        }
	        final Celda otra = (Celda) obj;
	        if (this.x != otra.x) {
	            return false;
	        }
	        if (this.y != otra.y) {
	            return false;
	        }
	        return true;
	    }
	    
	    public JSONObject getJSON(){
	    	JSONObject json = new JSONObject();
	    	List<Boolean> neighbours = new ArrayList<>();
	    	neighbours.add(norte);
	    	neighbours.add(este);
	    	neighbours.add(sur);
	    	neighbours.add(oeste);
	    	
	    	json.put("value", 0);
	    	json.put("vecinos", neighbours);
	    	
	    	return json; 	
	    }
}
