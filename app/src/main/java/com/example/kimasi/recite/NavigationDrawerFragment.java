package com.example.kimasi.recite;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment  {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */    //一个可控制抽屉事件的ActionBar ,实现了抽屉的监听器DrawerListener
    private ActionBarDrawerToggle mDrawerToggle;//抽屉触发器,回调一些DrawerLayout.DrawerListener(他的子类)

    private DrawerLayout mDrawerLayout;//抽屉布局
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    static TextView userNameView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;//
    private boolean mUserLearnedDrawer;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    static String userName_N=" hhh";


    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);//应该是监听,抽屉是否打开

        if (savedInstanceState != null) {//获取之前退出时保存的数据
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);//选中的项
            mFromSavedInstanceState = true;
        }

        // 设置首先启动第一个列表项的碎片
        selectItem(mCurrentSelectedPosition); //设置选中项(初始值是0)或者之前退出时保存的,如上
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//在activity的onCreared()之后调用
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);//应该是设置对action bar影响(菜单项)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,//绘制界面时调用,返回listView
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_navigation_drawer,container,false);

        userNameView=(TextView)view.findViewById(R.id.user_Name);
        preferences=getActivity().getSharedPreferences("fpeizhi", getActivity().MODE_PRIVATE);//保存配制到本地
        userName_N=preferences.getString("user_name","kao");//可以读取其他类保存的设置
   //     userName_N=MainActivity.userName;
        userNameView.setText(userName_N);
    //    editor.putString("userName",userName_N);//试试看存在其他地方能不能获取
        Log.v("碎片creaView","============================");
        View fnd=view.findViewById(R.id.fnd_1);
        View touxiang=view.findViewById(R.id.touxiang);
        touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进用户界面或注册
                Intent intent;
                if(!MainActivity.zhuce){
                    intent=new Intent(getActivity(),LoginActivity.class);//登陆
                }else {
                     intent=new Intent(getActivity(),UserActivity.class);//资料
                }
                startActivity(intent);
            }
        });
        fnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //拦截这个侧边栏的监听使不会传递到下一层的Fragment;

            }
        });
        mDrawerListView=(ListView)view.findViewById(R.id.na_dra_list);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);//根据点击启动碎片
            }
        });
        mDrawerListView.setAdapter(new ArrayAdapter<String>( //设置列表的简单适配器
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{
                        getString(R.string.title_section1),
                        getString(R.string.title_section2),
                        getString(R.string.title_section3),
                }));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);//设置列表项只能选择一个

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //系统版本,容错
            view.setPadding(0, 150, 0, 0);  //安卓4.4以上,不然会被覆盖,(透明状态栏)
        }
        return  view;
    }


    public boolean isDrawerOpen() {  //检查抽屉是否打开
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);//被drawerLayout控制的view(就是这里的listView碎片)
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);//应该是设置抽屉打开时,空白部分的覆盖内容
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//设置是否显示抽屉的按钮
        actionBar.setHomeButtonEnabled(true);//  ??设置显示actionBar上的主页按钮

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(// 动作条抽屉切换器 将 抽屉 与 app icon动作条 绑定正确的交互。
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {// 当抽屉关闭时
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {// 如果该Fragment对象没有被添加到了drawerView中
                    return;  //测试:屏蔽这段代码后效果一样
                }//碎片有打开就显示另外的标题栏文字,如下(更新)

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }//应该是碎片没有显示在他的activity的时候

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu() 抽屉打开时改变标题栏上的文字(更新)
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);//打开抽屉
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {  //应该是启动一个ui线程
            @Override
            public void run() {
                mDrawerToggle.syncState();//应该是同步抽屉的状态
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);//设置一个监听来通知抽屉事件(这个监听是mDrawerToggle)
    }

    private void selectItem(int position) { //列表项的点击,开启一个碎片
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);//设置listView列表项为选中的状态；
        }
        if (mDrawerLayout != null) { //应该是设置点击完关闭抽屉
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {   //数据传给activity(传的点击项,会启动碎片或替换)
            mCallbacks.onNavigationDrawerItemSelected(position);//调用接口,在activity实现了,在onAttach()时已经确定activity对象
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();  //这个没显示右边的标题
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_example) {  //菜单栏,设置,外显
  //          Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
