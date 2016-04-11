package com.example.kimasi.recite;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Fragment3 extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    static List<String> list10 = new ArrayList<>();
    static List<String> list11 = new ArrayList<>();
    static List<String> list12 = new ArrayList<>();
    static List<String> list13 = new ArrayList<>();

    MyBaseAdapter myBaseAdapter;

    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(int sectionNumber) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment3() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_main3, container, false);
        final ListView listVie=(ListView)view.findViewById(R.id.fff3);
        myBaseAdapter=new MyBaseAdapter();
        listVie.setAdapter(myBaseAdapter);
        listVie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p=ReciteInfo.allWords.get(position).getEn();
                ReciteInfo.getReciteInfo().getPronounce().pronounce(p);//发声
            }
        });
        return view;
    }
    private class MyBaseAdapter extends BaseAdapter  {
        @Override
        public int getCount() {
            return list11.size();
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getActivity(), R.layout.list_item2, null);
            TextView textView1 = (TextView) view.findViewById(R.id.textView11);
            TextView textView2 = (TextView) view.findViewById(R.id.textView22);
            textView1.setText(list11.get(position));
            textView2.setText(list12.get(position));
            return view;
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
    @Override
    public void onDetach() {
        super.onDetach();

        Log.v("碎片离开","ondeta");
        mListener = null; //回调接口
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
         void onFragmentInteraction(Uri uri);
    }

}
