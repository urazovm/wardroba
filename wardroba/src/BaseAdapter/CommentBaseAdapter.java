package BaseAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.connection.WebAPIHelper;
import com.connection.WebAPIHelper1;
import com.example.wardroba.Comment;
import com.example.wardroba.CommentViewActivity;
import com.example.wardroba.HomeTabActivity;
import com.example.wardroba.LoginActivity;
import com.example.wardroba.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommentBaseAdapter extends BaseAdapter  
{
	LayoutInflater mInflater;
	Activity activity;
	
	List<Comment> arr_CommentList;	
	ImageLoader imageLoader;
	public GroupItem item ;
	AlertDialog.Builder builder;
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
			final Comment commentData=arr_CommentList.get(position);
	         
			vi=mInflater.inflate(R.layout.comment_view_activity_lay, null);
			
			RelativeLayout close_lay=(RelativeLayout)vi.findViewById(R.id.lay_close);
			close_lay.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					builder=new AlertDialog.Builder(activity);
					builder.setMessage("Are you sure you want to delete this comment?");
					builder.setPositiveButton("YES", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							// TODO Auto-generated method stub
							//http://41.wardroba.amp/serviceXml/comment.php?object_id=72&user_id=2&id_comment=67&cloth_type=item
							dialog.dismiss();
							Constants.SELECTED_COMMENT=position;
							WebAPIHelper1 apiHelper1=new WebAPIHelper1(Constants.comment_delete, activity);
							String delete_url = Constants.VIEW_COMMENT_URL+"&object_id="+Constants.OBJECT_ID+"&user_id="+Constants.CLOTH_USERID+"&id_comment="+commentData.getComment_id()+"&cloth_type="+Constants.CLOTH_TYPE;
							Log.d("CommentView", "comment delete url="+delete_url);
							apiHelper1.execute(delete_url);
						}
					}).setNegativeButton("NO", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					builder.create().show();
					
				}
			});
			
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
			return vi;	
	}	
	
	
}
