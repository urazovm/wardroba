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
		
		public ImageView imgUserPhoto;
		public TextView txtNameSurname,txtDate;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		 TextView txtLikeCount,txtCommentCount,txtShortDiscription;
		 final ImageView imgProductImage;
		 final ProgressBar progressBar;
		 ImageView btnLike,btnComment,btnShare;
		View vi=null;
			item =new GroupItem();
			 	
			vi=mInflater.inflate(R.layout.home_activity_lay, null);
			item.imgUserPhoto=(ImageView)vi.findViewById(R.id.img_user_photo);
		    item.txtNameSurname=(TextView)vi.findViewById(R.id.txt_name_surname);
		    item.txtDate=(TextView)vi.findViewById(R.id.txt_date);
			vi.setTag(item);
			item.txtNameSurname.setText(Constants.USER_NAME.toString());
			item.txtDate.setText(Constants.USER_DATE.toString());
			
			
			txtLikeCount =(TextView) vi.findViewById(R.id.txt_like);
			txtCommentCount =(TextView) vi.findViewById(R.id.txt_comment);
			txtShortDiscription =(TextView) vi.findViewById(R.id.txt_short_description);
			imgProductImage =(ImageView) vi.findViewById(R.id.img_product_photo);
			progressBar=(ProgressBar)vi.findViewById(R.id.progressBar1);
			btnLike =(ImageView) vi.findViewById(R.id.img_like);
			btnComment =(ImageView) vi.findViewById(R.id.img_comment);
			btnShare =(ImageView) vi.findViewById(R.id.img_share);
			
			txtLikeCount.setTypeface(tf);
			txtCommentCount.setTypeface(tf);
			txtShortDiscription.setTypeface(tf);
			
			txtLikeCount.setText(String.valueOf(arr_ProductList.get(position).PLikeCount));
			txtCommentCount.setText(String.valueOf(arr_ProductList.get(position).PCommentCount));
			txtShortDiscription.setText(arr_ProductList.get(position).PShortDescription.toString().trim());

			
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
							imgProductImage.post(new Runnable()
							{
								public void run() 
								{
									progressBar.setVisibility(View.GONE);		
								    imageLoader.DisplayImage(url,imgProductImage);								 
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
				progressBar.setVisibility(View.GONE);
				//item.imgProductImage.setImageResource(R.drawable.noimageavailable);
			}			
			
			btnLike.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Toast.makeText(activity,"Like", 5000).show();
				}
			});
		
		

			btnComment.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Toast.makeText(activity,"Comment", 5000).show();
				}
			});
		

			btnShare.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					Toast.makeText(activity,"Share", 5000).show();
				}
			});
		
		return vi;	
	}	
	
	
	private static class ViewHolder 
	{
		TextView header;
		int previousTop = 0;
	}

		
}
