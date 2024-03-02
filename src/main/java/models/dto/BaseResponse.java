package models.dto;

public class BaseResponse {
	private boolean result;
	private String message;
	
	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    

	
	public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}