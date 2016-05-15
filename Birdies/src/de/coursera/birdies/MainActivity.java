package de.coursera.birdies;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemSelectedListener {

	ImageView image;
	TextView textView;
	Spinner spinner;
	MediaPlayer player;
	String url = "https://en.wikipedia.org/wiki/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setSpinner();
	}

	// populate the spinner with values set in strings.xml
	private void setSpinner() {
		this.spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.birds, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinner.setAdapter(adapter);
		this.spinner.setOnItemSelectedListener(this);
	}

	// this method is called whenever a bird is chosen via the spinner
	// it then changes the picture, text and sound
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		TextView t = (TextView) view; // child of spinner, not the classes
										// textView!
		String bird = t.getText().toString();
		setImage(bird);
		setSound(bird);
		setText(bird);
	}

	// change picture to selected bird
	private void setImage(String bird) {
		this.image = (ImageView) findViewById(R.id.imageView);
		Context context = this.image.getContext();
		int id = context.getResources().getIdentifier(bird, "drawable",
				context.getPackageName());
		this.image.setImageResource(id);
	}

	// change sound to selected bird
	private void setSound(String bird) {
		stopPlayer();
		int song = getId(bird, R.raw.class);
		player = MediaPlayer.create(this, song);
		player.start();
	}

	// change text to selected bird
	private void setText(String bird) {
		this.textView = (TextView) findViewById(R.id.textView);
		int text = getId(bird, R.string.class);
		this.textView.setText(text);
	}

	// stop and release MediaPlayer. Called by onPause, onStop and setSound
	private void stopPlayer() {
		if (player != null) {
			player.stop();
			player.release();
		}
	}

	// get ResourceID from ResourceName for different Classes (i.e. Drawable,
	// Raw...)
	private int getId(String resourceName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(resourceName);
			return idField.getInt(idField);
		} catch (Exception e) {
			Log.e("getID", "No resource ID found for: " + resourceName + " / "
					+ c);
		}
		return 0;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

	public void onPause(View v) {
		Log.d("Pause", v.toString());
		stopPlayer();
		super.onPause();
	}

	public void onStop() {
		super.onStop();
		stopPlayer();
	}
}
