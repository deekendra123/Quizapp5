package com.aptitude.education.e2buddy.Menu;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.aptitude.education.e2buddy.R;

import java.util.HashMap;
import java.util.List;

public class
FAQ_ListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child titlex
    private HashMap<String, List<String>> _listDataChild;



    public FAQ_ListAdapter(Context _context, List<String> _listDataHeader, HashMap<String, List<String>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.faq_item, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);

        //    Toast.makeText(_context, ""+ groupPosition,Toast.LENGTH_SHORT).show();
        if (groupPosition == 1){
            String firest = childText.substring(0,40);
            // String send = childText.substring(71,81);
            String end = childText.substring(51,65);

            String next = "<br><font color='#EE0000'><b><u>Click here</u></b></font></br>";

            txtListChild.setText(Html.fromHtml(firest +" "+ next+ end));

            txtListChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = ((AppCompatActivity)_context).getFragmentManager();
                    DialogFragment dialog = Merchandise_Dialog.newInstance();
                    dialog.show(fm,"tag");

                }
            });

        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.faq_group, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.lblListHeader);

        Typeface type = Typeface.createFromAsset(_context.getAssets(), "fonts/Roboto-Regular.ttf");
        lblListHeader.setTypeface(type);

        lblListHeader.setText(headerTitle);



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
