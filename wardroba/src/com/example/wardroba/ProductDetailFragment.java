package com.example.wardroba;

import com.ImageLoader.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailFragment extends Fragment
{
	ImageView imgProductPhoto,imgShare;
	TextView txtLike,txtComment;
	ImageLoader imageLoader;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.product_detail_lay, null);
		imgProductPhoto=(ImageView)root.findViewById(R.id.img_product_photo);
		txtLike=(TextView)root.findViewById(R.id.txt_like);
		txtComment=(TextView)root.findViewById(R.id.txt_comment);
		imgShare=(ImageView)root.findViewById(R.id.img_share);
		imageLoader=new ImageLoader(getActivity());
		return root;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Toast.makeText(getActivity(), "On start", Toast.LENGTH_SHORT).show();
		 Bundle args = getArguments();
	        if (args != null) {
	            // Set article based on argument passed in
	        	Toast.makeText(getActivity(), "get argument", Toast.LENGTH_SHORT).show();
	            updateProductDetail(args);
	        }
	}
	private void updateProductDetail(Bundle args)
	{
		String imageUrl;
		int likeCount,commentCount;
		imageUrl=args.getString("image_url");
		likeCount=args.getInt("like_count");
		commentCount=args.getInt("comment_count");
		imageLoader.DisplayImage(imageUrl, imgProductPhoto);
		txtLike.setText(String.valueOf(likeCount));
		txtComment.setText(String.valueOf(commentCount));
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		ImageView btnBack;
		btnBack=(ImageView)activity.findViewById(R.id.btnBack);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});
		
		super.onAttach(activity);
	}
	
}
