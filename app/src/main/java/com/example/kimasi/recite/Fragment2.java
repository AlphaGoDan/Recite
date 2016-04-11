package com.example.kimasi.recite;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.kimasi.recite.module.Word;

import java.util.ArrayList;

public class Fragment2 extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;

    MyBaseAdapter myBaseAdapter = new MyBaseAdapter();

    Button paixu;
    Boolean shunxu = true;

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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main2, container, false);
        final ListView listVie = (ListView) view.findViewById(R.id.ffff);

        paixu = (Button) view.findViewById(R.id.paixu);

        myBaseAdapter = new MyBaseAdapter();
        listVie.setAdapter(myBaseAdapter);
        listVie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("列表项的单击", position + "");
            }
        });
        listVie.setTextFilterEnabled(true);  //激活文本过滤

        paixu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ReciteInfo.order==ReciteInfo.ASC) {
                    ReciteInfo.order = ReciteInfo.DESC;
                    listViewWords.clear();
                    listViewWords =ReciteInfo.getReciteInfo().getDataStorage().getAllWorld();
                //    ReciteInfo.getReciteInfo().upAllWords();
                    myBaseAdapter.notifyDataSetChanged();
                } else {
                    ReciteInfo.order = ReciteInfo.ASC;
                    shunxu = true;
                    listViewWords.clear();
                    listViewWords =ReciteInfo.getReciteInfo().getDataStorage().getAllWorld();
                //    ReciteInfo.getReciteInfo().upAllWords();
                    myBaseAdapter.notifyDataSetChanged();
                }
            }
        });

        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
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
                if (TextUtils.isEmpty(newText)) {
                    listVie.clearTextFilter();
                } else {
                    listVie.setFilterText(newText);
                }
                return true;
            }
        });

        return view;
    }

    ArrayList<Word> listViewWords = ReciteInfo.getReciteInfo().getDataStorage().getAllWorld();

    private class MyBaseAdapter extends BaseAdapter implements Filterable {
        private MyFilter myFilter;

        @Override
        public int getCount() {
            return listViewWords.size();
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

            View view = View.inflate(getActivity(), R.layout.list_item, null);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            TextView textView2 = (TextView) view.findViewById(R.id.textView2);
            textView1.setText(listViewWords.get(position).getEn());
            textView2.setText(listViewWords.get(position).getCn());
            //  if(ReciteInfo.passWords.get(position).getPass().equals("1")){
            //      view.setBackgroundColor(Color.parseColor("#FF7547"));//???修改某个列表项的颜色
            //   }
            return view;
        }

        @Override
        public Filter getFilter() {
            if (null == myFilter) {
                myFilter = new MyFilter();
//                System.out.println("测试--getFilter");
            }
            return myFilter;
        }

        class MyFilter extends Filter {  //listView适配器的过滤器 ,要注册
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String filterString = constraint.toString().trim()
                        .toLowerCase();
                ArrayList<Word> tem = new ArrayList<Word>();
                // 如果搜索框内容为空，就恢复原始数据
                if (TextUtils.isEmpty(filterString)) {
                    tem.clear();
                    tem = listViewWords;
                } else {
                    // 过滤出新数据
                    for (int i = 0; i < ReciteInfo.allWords.size(); i++) {       //转为小写,判断搜索内容与单词的匹配   或者用 boolean contains(CharSequence s)
                        if (-1 != ReciteInfo.allWords.get(i).getEn().toLowerCase().indexOf(filterString)) {
                            tem.add(ReciteInfo.allWords.get(i));
                        }
                    }
                }
                results.values = tem;    //过滤好的内容
                results.count = tem.size();
                return results;  //传出过滤好的数据
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                listViewWords = (ArrayList<Word>) results.values;
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
