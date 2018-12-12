package com.ichsy.libs.core.comm.logwatch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ichsy.core_library.R;
import com.ichsy.libs.core.comm.logwatch.ContentAdapter;
import com.ichsy.libs.core.comm.logwatch.InfoBean;
import com.ichsy.libs.core.comm.logwatch.MyWindowManager;
import com.ichsy.libs.core.comm.logwatch.db.DAO;

import java.util.ArrayList;

public class FloatContentView extends LinearLayout implements OnClickListener {
	
	public int mWidth;
	public int mHeight;
	private MyWindowManager manager;
	private ContentAdapter adapter;
	private Button mBtnClose;
	private Button mBtnBack;
	private ListView mListView;
	private static ArrayList<InfoBean> mArrayList;
	private DAO dao;
	private LinearLayout mHorizontalLayout;
	private TextView mTextClear;
	private Context mContext;

	public FloatContentView(Context context) {
		this(context, null);
	}

	public FloatContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.logwatch_float_content_view, this);
		manager = MyWindowManager.getInstance(context);
		ViewGroup.LayoutParams params = findViewById(R.id.content).getLayoutParams();
		mWidth = params.width;
		mHeight = params.height;

		mBtnClose = (Button) findViewById(R.id.bt_close);
		mBtnBack = (Button) findViewById(R.id.bt_back);
		mListView = (ListView) findViewById(R.id.list);
		mHorizontalLayout = (LinearLayout)findViewById(R.id.horizontal_view);
		mTextClear = (TextView)findViewById(R.id.clear);

		mBtnClose.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		mTextClear.setOnClickListener(this);
		if(dao == null){
			dao = new DAO(context);
		}
		mArrayList = dao.query();//始终读取数据库里的内容
		ArrayList<String> list = new ArrayList<String>();
		boolean flag = false;
		for (int j = 0; j < mArrayList.size(); j++) {
			if(j == 0){
				list.add(mArrayList.get(0).getClassesName());
			}
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(mArrayList.get(j).getClassesName())) {
					flag = true;
					break;
				} else {
					flag = false;
				}
			}
			if(!flag){
				list.add(mArrayList.get(j).getClassesName());
			}
		}
		mHorizontalLayout.removeAllViews();
		if(list.size() != 0){
			View view = LinearLayout.inflate(context, R.layout.logwatch_content_btn, null);
			final TextView mTextName = (TextView) view.findViewById(R.id.text);
			mTextName.setText("全部");
			mHorizontalLayout.addView(view);
			mTextName.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mArrayList.clear();
					mArrayList = dao.query();
					setData(mContext);
				}
			});
		}
		for(int i=0;i<list.size();i++){
			View view1 = LinearLayout.inflate(context, R.layout.logwatch_content_btn, null);
			final TextView mTextName1 = (TextView) view1.findViewById(R.id.text);
			mTextName1.setText(list.get(i));
			mHorizontalLayout.addView(view1);
			mTextName1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String classesName = mTextName1.getText().toString();
					mArrayList.clear();
					mArrayList = dao.query(classesName);
					setData(mContext);
				}
			});
		}
		setData(context);
	}

	private void setData(final Context context) {
		adapter = new ContentAdapter(context);
		adapter.addData(mArrayList);
		mListView.setAdapter(adapter);
		mListView.setSelection(mArrayList.size()-1);
	}
	
	@Override
	public void onClick(View v) {
		if (v == mBtnClose) {
			manager.dismiss();
		} else if (v == mBtnBack) {
			manager.back();
		}else if(v == mTextClear){
			if (dao == null) {
				dao = new DAO(mContext);
			}
			dao.delete();
			mArrayList.clear();
			setData(mContext);
			mHorizontalLayout.removeAllViews();
		}
	}

}
