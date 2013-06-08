package de.alosdev.rebel.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class DemoDetails implements Parcelable{

	public final int demoId;
	public final String title;
	public final String description;

	public DemoDetails(int demoId, String title, String description) {
		this.demoId = demoId;
		this.title = title;
		if (null == description)
			this.description = "";
		else
			this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (null == o || !(o instanceof DemoDetails))
			return false;
		return demoId == ((DemoDetails) o).demoId;
	}

	@Override
  public int describeContents() {
	  return 0;
  }

	@Override
  public void writeToParcel(Parcel dest, int flags) {
	  dest.writeInt(demoId);
	  dest.writeString(title);
	  dest.writeString(description);
  }
	
	public DemoDetails(Parcel in) {
		demoId = in.readInt();
		title = in.readString();
		description = in.readString();
  }

	 public static final Parcelable.Creator<DemoDetails> CREATOR  = new Parcelable.Creator<DemoDetails>() {
	          public DemoDetails createFromParcel(Parcel in) {
	              return new DemoDetails(in);
	          }
	 
	          public DemoDetails[] newArray(int size) {
	              return new DemoDetails[size];
	          }
	      };
}