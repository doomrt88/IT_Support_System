package models;
public class Enums {

	public enum States {
        OPEN("Open"), IN_PROGRESS("In Progress"), RESOLVED("Resolved"), CLOSED("Closed");

        private final String status;

        States(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
	
    public enum Prirority {
    	PRIORITY_1(1),
    	PRIORITY_2(2),
    	PRIORITY_3(3);
    	
    	private final int value;

    	Prirority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}