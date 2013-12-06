package BaseAdapter;


import java.util.ArrayList;
import java.util.List;

import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.example.wardroba.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeProductBaseAdapter extends BaseAdapter
{
	LayoutInflater mInflater;
	Activity activity;
	List<Constants> arr_ProductList;	
	ImageLoader imageLoader;
	public GroupItem item ;
	Typeface tf;
	
	public HomeProductBaseAdapter(ArrayList<Constants> arr_category,Activity activity)
	{
		this.activity=activity;
		this.arr_ProductList=arr_category;
		mInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity);
		 tf= Typeface.createFromAsset(activity.getAssets(),"fonts/GOTHIC.TTF");
	}

	public int getCount()
	{
		return arr_ProductList.size();
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
		public TextView txtLikeCount,txtCommentCount,txtShortDiscription;
		public ImageView imgProductImage;
		public ProgressBar progressBar;
		public ImageView btnLike,btnComment,btnShare;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		View vi=null;
			item =new GroupItem();
			 	
			vi=mInflater.inflate(R.layout.home_activity_lay, null);
			
			item.txtLikeCount =(TextView) vi.findViewById(R.id.txt_like);
			item.txtCommentCount =(TextView) vi.findViewById(R.id.txt_comment);
			item.txtShortDiscription =(TextView) vi.findViewById(R.id.txt_short_description);
			item.imgProductImage =(ImageView) vi.findViewById(R.id.img_product_photo);
			item.progressBar=(ProgressBar)vi.findViewById(R.id.progressBar1);
			item.btnLike =(ImageView) vi.findViewById(R.id.img_like);
			item.btnComment =(ImageView) vi.findViewById(R.id.img_comment);
			item.btnShare =(ImageView) vi.findViewById(R.id.img_share);
			
			item.txtLikeCount.setTypeface(tf);
			item.txtCommentCount.setTypeface(tf);
			item.txtShortDiscription.setTypeface(tf);
			
			item.txtLikeCount.setText(String.valueOf(arr_ProductList.get(position).PLikeCount));
			item.txtCommentCount.setText(String.valueOf(arr_ProductList.get(position).PCommentCount));
			item.txtShortDiscription.setText(arr_ProductList.get(position).PShortDescription.toString().trim());

			
			//item.imgProductImage.setScaleType(ImageView.ScaleType.FIT_XY);
			
			String temp=arr_ProductList.get(position).PImageUrl;
			if (temp.length()>4)
			{		
			    final String url = temp;
				new Thread(new Runnable() 
				{
					public void run() 
					{
						try 
						{
							item.imgProductImage.post(new Runnable()
							{
								public void run() 
								{
									item.progressBar.setVisibility(View.GONE);		
								    imageLoader.DisplayImage(url, item.imgProductImage);								 
								}
							}); 
						} 
						catch (Exception e)
						{
							e.toString();
						}
					}
				}).start();
			}
			else
			{
				item.progressBar.setVisibility(View.GONE);
				//item.imgProductImage.setImageResource(R.drawable.noimageavailable);
			}			
			
			item.btnLike.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Toast.makeText(activity,"Like", 5000).show();
				}
			});
		
		

			item.btnComment.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Toast.makeText(activity,"Comment", 5000).show();
				}
			});
		

			item.btnShare.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Toast.makeText(activity,"Share", 5000).show();
				}
			});
		
		return vi;	
	}	
	

		
}
