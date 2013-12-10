package com.example.wardroba;

import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.connection.WebAPIHelper1;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductDetailFragment extends Fragment
{
	ImageView imgProductPhoto,imgShare,imgLike,imgComment;
	TextView txtLike,txtComment,textDescription;
	ImageLoader imageLoader;
	LinearLayout shareDialog;
	WardrobaItem selected_item;
	public static int SELECTED_PRODUCT=0;
	Typeface tf;
	@SuppressWarnings("unchecked")
  	public void setResponseFromRequest1(int requestNumber) 
  	{	
  			/*String status=Constants.LIKE_STATUS.toString().trim();
  			if(status.equals("LIKE"))
  			{
  				arr_productList.get(SELECTED_PRODUCT).GLikeStatus="UNLIKE";
  	  			arr_productList.get(SELECTED_PRODUCT).GLikeCount=Constants.LIKE_COUNT;
  			}else
  			{
  				arr_productList.get(SELECTED_PRODUCT).GLikeStatus="LIKE";
  	  			arr_productList.get(SELECTED_PRODUCT).GLikeCount=Constants.LIKE_COUNT;
  			}*/
			int cloth_id=Constants.my_items.get(Constants.SELECTED_ID).getPIdCloth();
			String status=Constants.my_items.get(Constants.SELECTED_ID).getPLikeStatus();
			int like_count=Constants.my_items.get(Constants.SELECTED_ID).getPLikeCount();
			int count=0;
  			if(Constants.all_items.size()>0)
  			{
  				for(WardrobaItem temp:Constants.all_items)
  				{
  					int id=temp.getPIdCloth();
  					if(id==cloth_id)
  						break;
  				
  					count++;	
  				}
  				
  			}
  			Constants.all_items.get(count).setPLikeStatus(status);
  			Constants.all_items.get(count).setPLikeCount(like_count);
  			updateProductDetail();
  	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.product_detail_lay, null);
		imgProductPhoto=(ImageView)root.findViewById(R.id.img_product_photo);
		txtLike=(TextView)root.findViewById(R.id.txt_like);
		txtComment=(TextView)root.findViewById(R.id.txt_comment);
		imgShare=(ImageView)root.findViewById(R.id.img_share);
		textDescription=(TextView)root.findViewById(R.id.txt_short_description);
		imgLike=(ImageView)root.findViewById(R.id.img_like);
		imgComment=(ImageView)root.findViewById(R.id.img_comment);
		shareDialog=(LinearLayout)root.findViewById(R.id.dialogShare);
		
		imageLoader=new ImageLoader(getActivity());
		tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
		txtLike.setTypeface(tf);
		txtComment.setTypeface(tf);
		textDescription.setTypeface(tf);
		initShareDialog(root);
		return root;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	//	Toast.makeText(getActivity(), "On start", Toast.LENGTH_SHORT).show();
		 Bundle args = getArguments();
	        if (args != null) {
	            // Set article based on argument passed in
	        	//Toast.makeText(getActivity(), "get argument", Toast.LENGTH_SHORT).show();
	            
	        }
	        updateProductDetail();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		updateProductDetail();
	}
	private void updateProductDetail()
	{
		String imageUrl,shortDesc;
		int likeCount,commentCount;
		/*imageUrl=args.getString("image_url");
		likeCount=args.getInt("like_count");
		commentCount=args.getInt("comment_count");
		shortDesc=args.getString("short_description");*/
		 selected_item=Constants.my_items.get(SELECTED_PRODUCT);
		imageUrl=selected_item.getPImageUrl();
		likeCount=selected_item.getPLikeCount();
		commentCount=selected_item.getPCommentCount();
		shortDesc=selected_item.getPShortDescription();
		imageLoader.DisplayImage(imageUrl, imgProductPhoto);
		txtLike.setText(String.valueOf(likeCount));
		txtComment.setText(String.valueOf(commentCount));
		textDescription.setText(shortDesc.trim());
		shareDialog.setVisibility(View.GONE);
		final String LikeStatus = selected_item.getPLikeStatus().toString().trim();
		if(LikeStatus.equals("LIKE"))
        {
			imgLike.setBackgroundResource(R.drawable.like);
        }else
        {
        	imgLike.setBackgroundResource(R.drawable.like_h);
        }
		imgLike.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String url;
				String Cloth_Id,User_Id,ObjectId1;
				Cloth_Id = String.valueOf( selected_item.getPIdCloth());
				User_Id = String.valueOf(selected_item.getPUserId());
				ObjectId1 = String.valueOf(selected_item.getPObjectId());
				Constants.SELECTED_ID=SELECTED_PRODUCT;
				
				int count_like = (selected_item.getPLikeCount());
		
				if(LikeStatus.equals("LIKE"))
		        {
					
					txtLike.setText(String.valueOf(count_like=count_like+1));
					imgLike.setBackgroundResource(R.drawable.like_h);
					url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Cloth_Id+"&user_id="+User_Id+"&object_id="+ObjectId1+"&status="+"LIKE";
		        }else
		        {
		        	txtLike.setText(String.valueOf(count_like=count_like-1));
		        	imgLike.setBackgroundResource(R.drawable.like);
		        	url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Cloth_Id+"&user_id="+User_Id+"&object_id="+ObjectId1+"&status="+"UNLIKE";
		        }
				try
				{
					WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.product_detail_like,ProductDetailFragment.this);
					Log.d("Like URL= ",url.toString());
					webAPIHelper.execute(url);    	
				}
				catch(Exception e)
				{
					
				}
			}
		});
		imgComment.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		imgShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareDialog.setVisibility(View.VISIBLE);
				Animation anim=AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_anim);
				anim.setFillAfter(true);
				shareDialog.startAnimation(anim);
			}
		});
	
	}
	public void setProductArray(int position)
	{
		
		SELECTED_PRODUCT=position;
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
	
	public void initShareDialog(View root)
	{
		ImageView fb,twt,pint,tmb,gplus;
		Button btnCancel;
		fb=(ImageView)root.findViewById(R.id.btnFB);
		twt=(ImageView)root.findViewById(R.id.btnTwitter);
		pint=(ImageView)root.findViewById(R.id.btnPinterest);
		tmb=(ImageView)root.findViewById(R.id.btnTumbler);
		gplus=(ImageView)root.findViewById(R.id.btnGplus);
		btnCancel=(Button)root.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				shareDialog.setVisibility(View.GONE);
				Animation anim=AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_anim);
				anim.setFillBefore(true);
				shareDialog.startAnimation(anim);
			}
		});
	}
}
