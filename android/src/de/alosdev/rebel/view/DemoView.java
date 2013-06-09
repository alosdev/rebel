/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.alosdev.rebel.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import de.alosdev.rebel.R;
import de.alosdev.rebel.domain.DemoDetails;

/**
 * A widget that describes an activity that demonstrates a feature.
 */
public final class DemoView extends FrameLayout {

    /**
     * Constructs a feature view by inflating layout/feature.xml.
     */
    public DemoView(Context context) {
        super(context);

        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.feature, this);
    }

    /**
     * Set the resource id of the title of the demo.
     *
     * @param title the resource id of the title of the demo
     */
    public synchronized void setTitleId(String title) {
        ((TextView) (findViewById(R.id.title))).setText(title);
    }

    /**
     * Set the resource id of the description of the demo.
     *
     * @param description the resource id of the description of the demo
     */
    public synchronized void setDescriptionId(String description) {
        ((TextView) (findViewById(R.id.description))).setText(description);
    }
    
    public synchronized DemoDetails getDemoDetails() {
    	Object obj = getTag();
    	if ( obj instanceof DemoDetails) {
    		return (DemoDetails) obj;
    	}
    	
    	return null;
    }
    
    public synchronized void setDemoDetails( DemoDetails details) {
    	setTag(details);
    }

}
