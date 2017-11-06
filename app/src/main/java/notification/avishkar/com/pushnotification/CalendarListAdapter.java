package notification.avishkar.com.pushnotification;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Prateeksha Singh on 11/6/2017.
 */

public class CalendarListAdapter extends BaseAdapter implements View.OnClickListener {

    private static Activity activity;
    private static ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;
    static AdvisorEvent tempValues = null;
    int i = 0;

    public CalendarListAdapter(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data = d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder {

        public TextView title;
        public TextView date;
        public TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.single_calendar_list_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.title = (TextView) vi.findViewById(R.id.eventTitle);
            holder.date = (TextView) vi.findViewById(R.id.eventDate);
            holder.time = (TextView) vi.findViewById(R.id.eventTime);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.title.setText("No Data");

        } else {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (AdvisorEvent) data.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.title.setText(tempValues.getName());
            holder.date.setText(tempValues.getDate());
            holder.time.setText(tempValues.getTime());

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            CalendarListActivity sct = (CalendarListActivity) activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }


}

