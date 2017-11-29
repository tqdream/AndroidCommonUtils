package main_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import androidcommonutil.main.R;


/**
 * @author taoqiang @create on 2016/10/10 14:16
 * @email tqdream@163.com
 */
public class MainActivity extends AppCompatActivity {

    private Button[] mTabs;

    private Fragment[] fragments;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private FourthFragment fourthFragment;

    private int index;
    private int currentTabIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();


        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        fourthFragment = new FourthFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,firstFragment).add(R.id.fragment_container,secondFragment).hide(secondFragment).show(firstFragment).commit();
        fragments = new Fragment[]{firstFragment, secondFragment, thirdFragment, fourthFragment};
    }

    private void initView() {
        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_conversation);
        mTabs[1] = (Button) findViewById(R.id.btn_address_list);
        mTabs[2] = (Button) findViewById(R.id.btn_find);
        mTabs[3] = (Button) findViewById(R.id.btn_profile);
        // select first tab
        mTabs[0].setSelected(true);
    }


    /**
     * on tab clicked
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_conversation:
                index = 0;
                break;
            case R.id.btn_address_list:
                index = 1;
                break;
            case R.id.btn_find:
                index = 2;
                break;
            case R.id.btn_profile:
                index = 3;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab selected
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }
}
