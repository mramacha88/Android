package iit.edu.mramacha_androidassignment3;

public class Building {
	// Instance Fields
	private String name;
	private String imageURL;
	private String buildingCode;
	private String buildingAddress;
	private String buildingDescription;

	// COnstructor with no-args
	public Building() {
		// TODO Auto-generated constructor stub
		name = "Nothing";
		imageURL = "No Image";
		buildingCode = "No Code";
		buildingAddress = "None";
		buildingDescription = "No Description";
	}

	// Accessors and Mutators
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getBuildingCode() {
		return buildingCode;
	}

	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}

	public String getBuildingAddress() {
		return buildingAddress;
	}

	public void setBuildingAddress(String buildingAddress) {
		this.buildingAddress = buildingAddress;
	}

	public String getBuildingDescription() {
		return buildingDescription;
	}

	public void setBuildingDescription(String buildingDescription) {
		this.buildingDescription = buildingDescription;
	}

}
