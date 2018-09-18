package magic8Ball.magic8BallService;

public class Magic8BallResponse {
	private String timestamp;
	private String response;

	public Magic8BallResponse(String timestamp, String response) {
		super();
		this.timestamp = timestamp;
		this.response = response;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}