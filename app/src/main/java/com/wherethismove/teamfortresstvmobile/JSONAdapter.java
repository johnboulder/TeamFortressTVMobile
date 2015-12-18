package com.wherethismove.teamfortresstvmobile;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Moak ~ found on http://stackoverflow.com/questions/6277154/populate-listview-from-json
 * on 11/25/2015.
 */
class JSONAdapter extends BaseAdapter implements ListAdapter
{

	private final Activity  activity;
	private final JSONArray jsonArray;

	private JSONAdapter(Activity activity, JSONArray jsonArray)
	{
		assert activity != null;
		assert jsonArray != null;

		this.jsonArray = jsonArray;
		this.activity = activity;
	}


	@Override
	public int getCount() {

		return jsonArray.length();
	}

	@Override public JSONObject getItem(int position) {

		return jsonArray.optJSONObject(position);
	}

	@Override public long getItemId(int position) {
		JSONObject jsonObject = getItem(position);

		return jsonObject.optLong("id");
	}

	@Override public View getView(int position, View convertView, ViewGroup parent) {

//		if (convertView == null)
//			convertView = activity.getLayoutInflater().inflate(R.layout.row, null);

		JSONObject jsonObject = getItem(position);

		return convertView;
	}
}
