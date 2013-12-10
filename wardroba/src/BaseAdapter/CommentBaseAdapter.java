package BaseAdapter;


import java.util.ArrayList;
import java.util.List;

import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.connection.WebAPIHelper1;
import com.example.wardroba.Comment;
import com.example.wardroba.CommentViewActivity;
import com.example.wardroba.HomeTabActivity;
import com.example.wardroba.LoginActivity;
import com.example.wardroba.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CommentBaseAdapter extends BaseAdapter  
{
	LayoutInflater mInflater;
	Activity activity;
	Comment commentData;
	List<Comment> arr_CommentList;	
	ImageLoader imageLoader;
	public GroupItem item ;
	Typeface tf;
		
  	public CommentBaseAdapter(ArrayList<Comment> arr_category,final Activity activity)
	{
		this.activity=activity;
		this.arr_CommentList=arr_category;
		mInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity);
		 tf= Typeface.createFromAsset(activity.getAssets(),"fonts/GOTHIC.TTF");
	}

	public int getCount()
	{
		return arr_CommentList.size();
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
		
		//public ImageView imgUserPhoto;
		public TextView txtName,txtDate,txtComment;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) 
	{

		 	//final ProgressBar progressBar;
		 	
		 	View vi=null;
			item =new GroupItem();
			commentData=arr_CommentList.get(position);
	         
			vi=mInflater.inflate(R.layout.comment_view_activity_lay, null);
			
		    item.txtName=(TextView)vi.findViewById(R.id.txt_user_name);
		    item.txtDate=(TextView)vi.findViewById(R.id.txt_date);
		    item.txtComment=(TextView)vi.findViewById(R.id.txt_comment);
		    
			vi.setTag(item);			
		
			//progressBar=(ProgressBar)vi.findViewById(R.id.progressBar1);
			item.txtName.setTypeface(tf);
			item.txtDate.setTypeface(tf);
			item.txtComment.setTypeface(tf);
			
			item.txtName.setText(String.valueOf(commentData.getStore_name().toString().trim()));
			item.txtDate.setText(String.valueOf(commentData.getDate().toString().trim()));
			item.txtComment.setText(commentData.getComment().toString().trim());
			
			
		
//			btnComment.setOnClickListener(new View.OnClickListener() 
//			{
//				public void onClick(View v) 
//				{
//					Constants.CLOTHISID = String.valueOf(arr_ProductList.get(position).PIdCloth);
//					Constants.CLOTH_USERID = String.valueOf(arr_ProductList.get(position).PUserId);
//					Constants.OBJECT_ID = String.valueOf(arr_ProductList.get(position).PObjectId);
//					Intent intent=new Intent(activity,CommentViewActivity.class);
//		   			activity.startActivity(intent);
//				}
//			});
//		

			
		return vi;	
	}	     
}
