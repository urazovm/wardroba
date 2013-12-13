package BaseAdapter;


import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.connection.WebAPIHelper1;
import com.example.wardroba.CommentViewActivity;
import com.example.wardroba.R;
import com.example.wardroba.SmartImageView;
import com.example.wardroba.WardrobaItem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeProductBaseAdapter extends BaseAdapter 
{
	LayoutInflater mInflater;
	Activity activity;
	
	ImageLoader imageLoader;
	public GroupItem item ;
	Typeface tf;
	
	

	String LikeStatus;
	String CommentId;
	WardrobaItem wardrobaItem;
	

  	public HomeProductBaseAdapter(Activity activity)
	{
		this.activity=activity;
		
		mInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity);
		 tf= Typeface.createFromAsset(activity.getAssets(),"fonts/GOTHIC.TTF");
		 
		
//	    animFadeIn=AnimationUtils.loadAnimation(activity, R.anim.fade_in);
//	    animFadeOut=AnimationUtils.loadAnimation(activity, R.anim.fade_out);	
	    
	}

	public int getCount()
	{
		return Constants.all_items.size();
	}

	public Object getItem(int position)  
	{
		// TODO Auto-generated method stub
		return null;
	}
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}
	class GroupItem
	{
		public ImageView imgUserPhoto;
		public TextView txtNameSurname,txtDate;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		 	final TextView txtLikeCount,txtCommentCount,txtShortDiscription;
		 	SmartImageView imgProductImage;
		 	final ProgressBar progressBar,progressBarUserPhoto;
		 	final ImageView btnLike,btnComment,btnShare;
		 	final ImageView imgLikeDil;
		 	wardrobaItem=Constants.all_items.get(position);
		 	View vi=null;
			item =new GroupItem();

	         
			vi=mInflater.inflate(R.layout.home_activity_lay, null);
			item.imgUserPhoto=(ImageView)vi.findViewById(R.id.img_user_photo);
		    item.txtNameSurname=(TextView)vi.findViewById(R.id.txt_name_surname);
		    item.txtDate=(TextView)vi.findViewById(R.id.txt_date);
			vi.setTag(item);			
			
			txtLikeCount =(TextView) vi.findViewById(R.id.txt_like);
			txtCommentCount =(TextView) vi.findViewById(R.id.txt_comment);
			txtShortDiscription =(TextView) vi.findViewById(R.id.txt_short_description);
			imgProductImage =(SmartImageView) vi.findViewById(R.id.img_product_photo);
			
			imgLikeDil =(ImageView) vi.findViewById(R.id.img_like_dil);
			progressBar=(ProgressBar)vi.findViewById(R.id.progressBar1);
			progressBarUserPhoto=(ProgressBar)vi.findViewById(R.id.progressBar2);
			btnLike =(ImageView) vi.findViewById(R.id.img_like);
			btnComment =(ImageView) vi.findViewById(R.id.img_comment);
			btnShare =(ImageView) vi.findViewById(R.id.img_share);
			
			txtLikeCount.setTypeface(tf);
			txtCommentCount.setTypeface(tf);
			txtShortDiscription.setTypeface(tf);
			item.txtNameSurname.setTypeface(tf);
			item.txtDate.setTypeface(tf);
			imgLikeDil.setVisibility(View.GONE);

			item.txtNameSurname.setText(wardrobaItem.getPUserName().toString().trim());
			item.txtDate.setText(wardrobaItem.getPUserDate().toString().trim());
			
			imageLoader.DisplayImage(wardrobaItem.getPUserImage().toString().trim(),item.imgUserPhoto,progressBarUserPhoto);
			progressBar.setVisibility(View.GONE);
			
			txtLikeCount.setText(String.valueOf(wardrobaItem.getPLikeCount()));
			txtCommentCount.setText(String.valueOf(wardrobaItem.getPCommentCount()));
			txtShortDiscription.setText(wardrobaItem.getPTag().toString().trim());
			
			String Status=wardrobaItem.getPLikeStatus().toString().trim();
			if(Status.equals("LIKE"))
	        {
				btnLike.setBackgroundResource(R.drawable.like);
	        }else
	        {
	        	btnLike.setBackgroundResource(R.drawable.like_h);
	        }
			
			String temp=wardrobaItem.getPImageUrl();
			imageLoader.DisplayImage(temp,imgProductImage,progressBar);
			progressBar.setVisibility(View.GONE);

			
			btnLike.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					String url;

					Constants.CLOTHISID = String.valueOf(wardrobaItem.getPIdCloth());
					Constants.CLOTH_USERID = String.valueOf(wardrobaItem.getPUserId());
					Constants.OBJECT_ID = String.valueOf(wardrobaItem.getPObjectId());
					Constants.CLOTH_TYPE= wardrobaItem.getPClothType().toString().trim();
					Log.d("Object id:", "object id:"+Constants.OBJECT_ID+" user id:"+Constants.CLOTH_USERID);
					
					LikeStatus = wardrobaItem.getPLikeStatus().toString().trim();
					Log.d("BaseAdapter", "Like status:"+LikeStatus);

					Constants.SELECTED_ID=position;
					int count_like = (wardrobaItem.getPLikeCount());
			
					if(LikeStatus.equals("LIKE"))
			        {
						txtLikeCount.setText(String.valueOf(count_like=count_like+1));
						btnLike.setBackgroundResource(R.drawable.like_h);
						url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Constants.CLOTHISID+"&user_id="+Constants.CLOTH_USERID+"&object_id="+Constants.OBJECT_ID+"&cloth_type="+Constants.CLOTH_TYPE+"&status="+"LIKE";
			        }else
			        {
			        	txtLikeCount.setText(String.valueOf(count_like=count_like-1));
			        	btnLike.setBackgroundResource(R.drawable.like);
			        	url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Constants.CLOTHISID+"&user_id="+Constants.CLOTH_USERID+"&object_id="+Constants.OBJECT_ID+"&cloth_type="+Constants.CLOTH_TYPE+"&status="+"UNLIKE";
			        }
					try
					{
						WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.product_like,activity);
						Log.d("Like URL= ",url.toString());
						webAPIHelper.execute(url);    	
					}
					catch(Exception e)
					{
						
					}	
				}
			});
		
		
			btnComment.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					
					Constants.CLOTHISID = String.valueOf(wardrobaItem.getPIdCloth());
					Constants.CLOTH_USERID = String.valueOf(wardrobaItem.getPUserId());
					Constants.OBJECT_ID = String.valueOf(wardrobaItem.getPObjectId());
					Constants.CLOTH_TYPE= wardrobaItem.getPClothType().toString().trim();
					Log.d("Object id:", "object id:"+Constants.OBJECT_ID+" user id:"+Constants.CLOTH_USERID);
					Intent intent=new Intent(activity,CommentViewActivity.class);
		   			activity.startActivity(intent);
				}
			});
		
			
			btnShare.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Toast.makeText(activity,"Share", 5000).show();
				}
			});
			imgProductImage.setDilImage(imgLikeDil);
			
			/*imgProductImage.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					//Toast.makeText(activity,"Photo", 5000).show();

					imgLikeDil.setVisibility(View.VISIBLE);
					Animation anim=AnimationUtils.loadAnimation(activity, R.anim.fade_in);
					//anim.setFillAfter(true);
					//anim.setRepeatMode(Animation.REVERSE);
					imgLikeDil.startAnimation(anim);
					anim.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub
							
							imgLikeDil.setVisibility(View.GONE);
							Animation anim=AnimationUtils.loadAnimation(activity, R.anim.fade_out);
							
							//anim.setRepeatMode(Animation.REVERSE);
							imgLikeDil.startAnimation(anim);
						}
					});
					
				}
			});*/
			
//			animFadeOut.setAnimationListener(new AnimationListener() 
//			{
//				
//				@Override
//				public void onAnimationStart(Animation animation) {
//					// TODO Auto-generated method stub
//					//imgLikeDil.setVisibility(View.GONE);
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onAnimationEnd(Animation animation) 
//				{
//					// TODO Auto-generated method stub
//					Toast.makeText(activity, "call start", Toast.LENGTH_SHORT).show();
//					
//						animFadeOut.start();
//						imgLikeDil.setVisibility(View.GONE);
//				}
//			});
		return vi;	
	}	
	
	
	private static class ViewHolder 
	{
		TextView header;
		int previousTop = 0;
	}

	 
	
	


	
     
}
