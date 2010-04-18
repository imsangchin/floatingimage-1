package dk.nindroid.rss.settings;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import dk.nindroid.rss.R;
import dk.nindroid.rss.parser.picasa.PicasaFeeder;

public class PicasaBrowser extends ListActivity {
	// Positions
	private static final int	SHOW_STREAM = 0;
	private static final int	SEARCH 		= 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fillMenu();
	}
	
	private void fillMenu(){
		String showStream = this.getResources().getString(R.string.picasaShowStream);
		String search = this.getResources().getString(R.string.picasaSearch);
		String[] options = new String[]{showStream, search};
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		FrameLayout fl = new FrameLayout(this);
		final EditText input = new EditText(this);

		fl.addView(input, FrameLayout.LayoutParams.FILL_PARENT);
		input.setGravity(Gravity.CENTER);
		switch(position){
		case SHOW_STREAM:
			final AlertDialog streamDialog = new AlertDialog.Builder(this)
				.setView(fl)
				.setTitle(R.string.picasaShowStreamUsername)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						returnStream(input.getText().toString());
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
			showKeyboard(streamDialog, input);
			streamDialog.show();
			break;
		case SEARCH:
			final AlertDialog searchDialog = new AlertDialog.Builder(this)
			.setView(fl)
			.setTitle(R.string.picasaSearchTerm)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					returnSearch(input.getText().toString());
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).create();
			showKeyboard(searchDialog, input);
			searchDialog.show();
			break;
		}
	}
	
	protected static void showKeyboard(final AlertDialog dialog, EditText editText){
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus) {
		        	dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		        }
		    }
		});
	}
	
	private void returnStream(String username){
		if(username.length() == 0){ // This actually returns a user with no images!
			Toast.makeText(this, R.string.picasaShowStreamNoUsername, Toast.LENGTH_LONG).show();
			return;
		}
		if(username == null) {// Bad username.
			Toast.makeText(this, R.string.picasaShowStreamBadUsername, Toast.LENGTH_LONG).show();
			return; 
		}
		Intent intent = new Intent();
		Bundle b = new Bundle();
		String streamURL = PicasaFeeder.getRecent(username);
		b.putString("PATH", streamURL);
		b.putString("NAME", "Stream: " + username);
		intent.putExtras(b);
		setResult(RESULT_OK, intent);		
		finish();
	}
	
	private void returnSearch(String criteria){
		if(criteria.length() == 0){
			Toast.makeText(this, R.string.picasaSearchNoText, Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent();
		Bundle b = new Bundle();
		String streamURL = PicasaFeeder.getSearchUrl(criteria);
		b.putString("PATH", streamURL);
		b.putString("NAME", "Search: " + criteria);
		intent.putExtras(b);
		setResult(RESULT_OK, intent);		
		finish();
	}
}