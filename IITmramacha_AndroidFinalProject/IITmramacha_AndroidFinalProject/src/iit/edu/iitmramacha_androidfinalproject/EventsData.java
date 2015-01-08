package iit.edu.iitmramacha_androidfinalproject;

public class EventsData {

	public String eventName;
	public String eventCategory;
	public String eventOrganizerName;
	public String eventAddress;
	public String eventLatitude;
	public String eventlongitude;
	public String eventStartDate;
	public String eventEndDate;
	public String eventURI;
	public String eventDescription;

	public EventsData() {

	}

	public EventsData(String eventName, String eventCategory,
			String eventOrganizerName, String eventAddress,
			String eventLatitude, String eventlongitude, String eventStartDate,
			String eventEndDate, String eventURI, String eventDescription) {
		this.eventName = eventName;
		this.eventCategory = eventCategory;
		this.eventOrganizerName = eventOrganizerName;
		this.eventAddress = eventAddress;
		this.eventLatitude = eventLatitude;
		this.eventlongitude = eventlongitude;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
		this.eventURI = eventURI;
		this.eventDescription = eventDescription;
	}

	public EventsData(String eventName, String eventOrganizerName,
			String eventAddress, String eventLatitude, String eventlongitude,
			String eventStartDate, String eventEndDate, String eventURI,
			String eventDescription) {
		this.eventName = eventName;
		this.eventOrganizerName = eventOrganizerName;
		this.eventAddress = eventAddress;
		this.eventLatitude = eventLatitude;
		this.eventlongitude = eventlongitude;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
		this.eventURI = eventURI;
		this.eventDescription = eventDescription;
	}

}