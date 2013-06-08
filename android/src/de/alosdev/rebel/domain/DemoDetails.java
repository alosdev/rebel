package de.alosdev.rebel.domain;

import java.util.ArrayList;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class DemoDetails implements Parcelable {

	public final int demoId;
	public final String title;
	public final String description;
	public final ArrayList<Location> route;

	public DemoDetails(int demoId, String title, String description, ArrayList<Location> route) {
		this.demoId = demoId;
		this.title = title;
		if (route == null)
			this.route = new ArrayList<Location>();
		else
			this.route = route;
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
		dest.writeList(route);
	}

	@SuppressWarnings("unchecked")
	public DemoDetails(Parcel in) {
		demoId = in.readInt();
		title = in.readString();
		description = in.readString();
		route = in.readArrayList(null);
	}

	public static final Parcelable.Creator<DemoDetails> CREATOR = new Parcelable.Creator<DemoDetails>() {
		public DemoDetails createFromParcel(Parcel in) {
			return new DemoDetails(in);
		}

		public DemoDetails[] newArray(int size) {
			return new DemoDetails[size];
		}
	};
}