package de.alosdev.rebel.domain;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Report implements Parcelable {

	public final String name;
	public final String report;
	public final Location location;

	public Report(String name, String report, Location location) {
		this.report = report;
		this.location = location;
		if (null == name)
			this.name = "anonymous";
		else
			this.name = name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(report);
		dest.writeValue(location);
	}

	public Report(Parcel in) {
		name = in.readString();
		report = in.readString();
		location = (Location) in.readValue(null);
	}

	public static final Parcelable.Creator<Report> CREATOR = new Parcelable.Creator<Report>() {
		public Report createFromParcel(Parcel in) {
			return new Report(in);
		}

		public Report[] newArray(int size) {
			return new Report[size];
		}
	};
}