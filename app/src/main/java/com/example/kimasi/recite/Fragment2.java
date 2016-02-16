package com.example.kimasi.recite;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fragment2 extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;

    static List<String> list20 = new ArrayList<String>();
    static List<String> list21 = new ArrayList<String>();
    static List<String> list22 = new ArrayList<String>();
    static List<String> list23 = new ArrayList<String>();

    static List<String> listd1 = new ArrayList<String>();
    static List<String> listd2 = new ArrayList<String>();

    MyBaseAdapter myBaseAdapter=new MyBaseAdapter();

    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(int sectionNumber) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLquanbu();
        listd1=list21;
        listd2=list22;
    }

    private void SQLquanbu(){
        Cursor cursor= MainActivity.mDb.rawQuery(
                "select * from dict where ? <= k",
                new String[]{"0"});
        while (cursor.moveToNext()){
//            list20.add(cursor.getString(0));
            list21.add(cursor.getString(1));
            list22.add(cursor.getString(2));
            list23.add(cursor.getString(3));
        }
//        System.out.println("测试**总数= " + list21.size());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_main2, container, false);
        final ListView listVie=(ListView)view.findViewById(R.id.ffff);

        myBaseAdapter=new MyBaseAdapter();
        listVie.setAdapter(myBaseAdapter);
        listVie.setTextFilterEnabled(true);  //激活文本过滤

        SearchView searchView=(SearchView)view.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查找");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    listVie.clearTextFilter();
                }
                else{
                    listVie.setFilterText(newText);
                }
                return true;
            }
        });

        return view;
    }

    private class MyBaseAdapter extends BaseAdapter implements Filterable {
        private MyFilter myFilter;

        @Override
        public int getCount() {
            return listd1.size();
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

            View view= View.inflate(getActivity(), R.layout.list_item,null);
            TextView textView1=(TextView)view.findViewById(R.id.textView1);
            TextView textView2=(TextView)view.findViewById(R.id.textView2);
            textView1.setText(listd1.get(position));
            textView2.setText(listd2.get(position));
            if(list23.get(position)=="1"){
                view.setBackgroundColor(Color.parseColor("#FF7547"));//???修改某个列表项的颜色
            }
            return view;
        }

        @Override
        public Filter getFilter(){
            if(null==myFilter){
                myFilter=new MyFilter();
//                System.out.println("测试--getFilter");
            }
            return myFilter;
        }

        class MyFilter extends Filter{  //listView适配器的过滤器 ,要注册

            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                List<String> newValues1 = new ArrayList<String>();
                List<String> newValues2 = new ArrayList<String>();

                HashMap<String,List> mapList=new HashMap<>();

                String filterString = constraint.toString().trim()
                        .toLowerCase();

                // 如果搜索框内容为空，就恢复原始数据
                if (TextUtils.isEmpty(filterString)) {
                    mapList.clear();
                    mapList.put("danci",list21);
                    mapList.put("fanyi",list22);
                } else {
                    // 过滤出新数据
                    for(int i=0;i<list21.size();i++)
                    {       //转为小写,判断搜索内容与单词的匹配
                        if (-1 != list21.get(i).toLowerCase().indexOf(filterString)) {
                            newValues1.add(list21.get(i));
                            newValues2.add(list22.get(i));
                        }
                    }
                    mapList.put("danci",newValues1);
                    mapList.put("fanyi",newValues2);
                }
                results.values = mapList;    //???
                results.count = newValues1.size();
                return results;  //传出过滤好的数据
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                HashMap<String,List> mapList2= (HashMap)results.values;

                listd1 = mapList2.get("danci");//赋给适配器的数据
                listd2 =mapList2.get("fanyi");

                if (results.count > 0) {
                   myBaseAdapter.notifyDataSetChanged();  // 通知数据发生了改变
                } else {
                    myBaseAdapter.notifyDataSetInvalidated(); // 通知数据失效
                }
            }
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
        list21.clear();
        list22.clear();
        list23.clear();
        mListener = null;
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
        public void onFragmentInteraction(Uri uri);
    }

}
