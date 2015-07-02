package me.glide.cursortest;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    @Bind(R.id.button_move_to_first)
    Button moveToFirstBtn;

    @Bind(R.id.button_move_to_position)
    Button moveToPositionBtn;

    @Bind(R.id.list)
    ListView list;

    MAdapter mAdapter;

    private ArrayList<String> mNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.button_move_to_first)
    public void moveToFirstMethod() {
        Cursor c = null;
        mNames.clear();
        if(mAdapter != null) {
            mAdapter.notifyDataSetInvalidated();
        }
        try {
            c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            Log.e("yishai", "query happened, I have " + c.getCount() + " results");
            c.moveToFirst();
            while(!c.isLast()){
                mNames.add(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                c.moveToNext();
            }
            if (!mNames.isEmpty()) {
                mAdapter = new MAdapter(mNames);
                list.setAdapter(mAdapter);
            } else {
                Toast.makeText(this, "no contacts biatch", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e) {
            Log.e("Yishai", "Failed to use cursor", e);
        }finally {
            if(c != null) {
                c.close();
            }
        }
    }

    @OnClick(R.id.button_move_to_position)
    public void clicked() {
        mNames.clear();
        Cursor c = null;
        if(mAdapter != null) {
            mAdapter.notifyDataSetInvalidated();
        }
        try {
            c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            Log.e("yishai", "query happened, I have " + c.getCount() + " results");
            c.moveToPosition(-1);
            while (!c.isLast()) {
                c.moveToNext();
                mNames.add(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            }
            if(mNames.isEmpty()) {
                Toast.makeText(this, "no contacts biatch", Toast.LENGTH_LONG).show();
            }else {
                mAdapter =  new MAdapter(mNames);
                list.setAdapter(mAdapter);
            }
        } catch (Exception e) {
            Log.e("Yishai", "Failed to use cursor", e);
        }finally {
            if(c != null) {
                c.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static class MAdapter extends BaseAdapter {

        private final ArrayList<String> mData;

        public MAdapter(ArrayList<String> data) {
            mData = new ArrayList<String>(data);
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData == null || mData.isEmpty() ? null : mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.text_item);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(mData.get(position));
            return convertView;
        }

        private static class ViewHolder {
            TextView name;
        }
    }
}
