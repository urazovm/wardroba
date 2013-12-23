package com.example.wardroba;


import com.ImageLoader.ImageLoader;
import com.connection.Constants;
import com.connection.WebAPIHelper;
import com.connection.WebAPIHelper1;
import com.example.wardroba.SmartImageView.onMyDoubleClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class HomeActivityFragment extends Fragment 
{
	public ListView lsvProductList;
	public ImageView imgUserPhoto;
	public TextView txtNameSurname,txtDate,txtSharLable;
	LinearLayout linOwnerHeader;
	Typeface tf;
	LinearLayout shareDialog;
	ImageView btnFacebook,btnTwitter,btnPinterest,btnTumbler,btnGooglePlus;
	Button btnCancel;
	HomeProductBaseAdapter adapter=null;
	
	
  	public void setResponseFromRequest(int requestNumber) 
  	{		  		 		
  		if(Constants.all_items.size()>0)
  		{
//  			txtNameSurname.setText(Constants.USER_NAME.toString());
//  			txtDate.setText(Constants.USER_DATE.toString());
  			adapter=new HomeProductBaseAdapter(HomeActivityFragment.this, shareDialog);
  			adapter.notifyDataSetChanged();
  			
 	    	lsvProductList.setAdapter(adapter);
 	    	lsvProductList.invalidateViews();
  		}
  		else
  		{
  			lsvProductList.setAdapter(null);
  			Toast.makeText(getActivity(), "No Record Found !", 5000).show();
  		}
  	} 	
  	public void setResponseFromRequest1(int requestNumber) 
  	{		
		if(adapter!=null)
		{
			adapter.notifyDataSetChanged();
		}
		if(Constants.my_items.size()>0)
		{
  			int cloth_id=Constants.all_items.get(Constants.SELECTED_ID).getPIdCloth();
			String status=Constants.all_items.get(Constants.SELECTED_ID).getPLikeStatus();
			int like_count=Constants.all_items.get(Constants.SELECTED_ID).getPLikeCount();
			int count=0;
  			if(Constants.my_items.size()>0)
  			{
  				for(WardrobaItem temp:Constants.my_items)
  				{
  					int id=temp.getPIdCloth();
  					if(id==cloth_id)
  						break;
  				
  					count++;	
  				}
  				
  			}
  			Constants.my_items.get(count).setPLikeStatus(status);
  			Constants.my_items.get(count).setPLikeCount(like_count);
		}
  	} 	
  	
  	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_activity, null);
        getActivity().findViewById(R.id.btnBackHome).setVisibility(View.GONE);
        tf= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GOTHIC.TTF");
        
        lsvProductList=(ListView)root.findViewById(R.id.product_list);
 	     
 		btnFacebook=(ImageView)root.findViewById(R.id.btnFB);
 		btnTwitter=(ImageView)root.findViewById(R.id.btnTwitter);
 		btnPinterest=(ImageView)root.findViewById(R.id.btnPinterest);
 		btnTumbler=(ImageView)root.findViewById(R.id.btnTumbler);
 		btnGooglePlus=(ImageView)root.findViewById(R.id.btnGplus);
 		btnCancel=(Button)root.findViewById(R.id.btnCancel);
 		txtSharLable=(TextView)root.findViewById(R.id.txt_share_lable);

 	    shareDialog=(LinearLayout)root.findViewById(R.id.dialogShare);
 	    shareDialog.setVisibility(View.GONE);
 	    btnCancel.setTypeface(tf);
 	    txtSharLable.setTypeface(tf);
 	  
 	    lsvProductList.setOnScrollListener(new OnScrollListener() 
		{
	        int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	        public void onScrollStateChanged(AbsListView view, int scrollState) 
	        {
	            mScrollState = scrollState;
	        }

	        @Override
	        public void onScroll(AbsListView list, int firstItem, int visibleCount, int totalCount) 
	        {
	            if (mScrollState == OnScrollListener.SCROLL_STATE_IDLE)
	                return;

	            for (int i=0; i < visibleCount; i++) 
	            {
	                View listItem = list.getChildAt(i);
	                if (listItem == null)
	                    break;

	                linOwnerHeader = (LinearLayout) listItem.findViewById(R.id.header);

	                int topMargin = 0;
	                if (i == 0) 
	                {
	                    int top = listItem.getTop();
	                    int height = listItem.getHeight();
	                    if (top < 0)
	                        topMargin = linOwnerHeader.getHeight() < (top + height) ? -top : (height - linOwnerHeader.getHeight());
	                }

	                // set the margin.
	                ((ViewGroup.MarginLayoutParams) linOwnerHeader.getLayoutParams()).topMargin = topMargin;

	                listItem.requestLayout();

	            }
	        }
	     });
 	  
	    
	    if(Constants.all_items.size()>0)
  		{
  			adapter=new HomeProductBaseAdapter(HomeActivityFragment.this, shareDialog);
  			adapter.notifyDataSetChanged();
  			
 	    	lsvProductList.setAdapter(adapter);
 	    	//lsvProductList.invalidateViews();
  		}
  		else
  		{
  			lsvProductList.setAdapter(null);
  			//Toast.makeText(getActivity(), "No Record Found !", 5000).show();
  		}
	    
	    
	    CancelSharDialog();
	    FacebookSharing();
	    TwitterSharing();
	    PinterestSharing();
	    TumblerSharing();
	    GooglePlusSharing();
	    
        return root;
    }
    
  	
 
    public void onCreate(Bundle savedInstanceState) 
    {
		//getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);       
	    
	    
	    
	    if(isOnline()==true)
		{
			try
			{
				WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.product_list,HomeActivityFragment.this ,"Please Wait....");
				String url = Constants.HOME_PRODUCT_URL+"&id="+Constants.LOGIN_USERID;	
				Log.d("Product_List= ",url.toString());
				webAPIHelper.execute(url);    	
			}
			catch(Exception e)
			{
				
			}				
		}
		else
		{
			alert();
		}

	   
	    
    }
    @Override
    public void onAttach(Activity activity) 
    {
    	super.onAttach(activity);
    	try 
    	{
        	activity.findViewById(R.id.btnBackHome).setVisibility(View.GONE);	
        }
    	catch (ClassCastException e) 
        {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
		
    }
    
    public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected()) 
		{
			return true;
		}
		return false;
	}
        
    
    
   public void onPause() 
   {
	super.onPause();	
   } 
   
   public void onResume() 
   {
	super.onResume();
	if(Constants.IS_PROFILE_CHANGED)
	{
		 if(isOnline()==true)
			{
				try
				{
					Constants.IS_PROFILE_CHANGED=false;
					WebAPIHelper webAPIHelper = new WebAPIHelper(Constants.product_list,HomeActivityFragment.this ,"Please Wait....");
					String url = Constants.HOME_PRODUCT_URL+"&id="+Constants.LOGIN_USERID;	
					Log.d("Product_List= ",url.toString());
					webAPIHelper.execute(url);    	
				}
				catch(Exception e)
				{
					
				}				
			}
			else
			{
				alert();
			}
	}
	else if(adapter!=null)
	{
		adapter.notifyDataSetChanged();
		lsvProductList.refreshDrawableState();
	}
}
   public void alert()
   {
	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Please check your internet connection...")
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                dialog.dismiss();			                
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();  
   }
   
   public void CancelSharDialog()
   {
		btnCancel.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				shareDialog.setVisibility(View.GONE);
				Animation anim=AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_anim);
				anim.setFillBefore(true);
				shareDialog.startAnimation(anim);
			}
		});
   }
   
   public void FacebookSharing()
   {
		btnFacebook.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
					Toast.makeText(getActivity(), "Facebook", 5000).show();
			}
		});
   }
   
   public void TwitterSharing()
   {
		btnTwitter.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(getActivity(), "Twitter", 5000).show();
			}
		});
   }
   public void PinterestSharing()
   {
		btnPinterest.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(getActivity(), "Pinterest", 5000).show();
			}
		});
   }
   public void TumblerSharing()
   {
		btnTumbler.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(getActivity(), "Tumbler", 5000).show();
			}
		});
   }
   public void GooglePlusSharing()
   {
		btnGooglePlus.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(getActivity(), "GooglePlus", 5000).show();
			}
		});
   }
   
   
   
   
   
   
   class HomeProductBaseAdapter extends BaseAdapter 
   {
   	LayoutInflater mInflater;
   	
   	LinearLayout SharDialog;
   	ImageLoader imageLoader;
   	public GroupItem item ;
   	Typeface tf;
   	HomeActivityFragment mContext;
   	
   	
   	String CommentId;
   	WardrobaItem wardrobaItem;
   	

     	public HomeProductBaseAdapter(HomeActivityFragment context , LinearLayout dialog1)
   	{
   		this.mContext=context;
   		this.SharDialog=dialog1;
   		mInflater = (LayoutInflater)mContext.getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
   		imageLoader=new ImageLoader(mContext.getActivity());
   		tf= Typeface.createFromAsset(mContext.getActivity().getAssets(),"fonts/GOTHIC.TTF");
   		 
   		
   		
//   	    animFadeIn=AnimationUtils.loadAnimation(activity, R.anim.fade_in);
//   	    animFadeOut=AnimationUtils.loadAnimation(activity, R.anim.fade_out);	
   	    
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
   		public LinearLayout linOwnerHeader;
   	}
   	
   	public View getView(final int position, View convertView, ViewGroup parent) 
   	{
   		 	final TextView txtLikeCount,txtCommentCount,txtShortDiscription;
   		 	SmartImageView imgProductImage;
   		 	  //int like_count=0;
   		 	 
   		 	final ProgressBar progressBar,progressBarUserPhoto;
   		 	final ImageView btnLike,btnComment,btnShare;
   		 	final ImageView imgLikeDil;
   		 	final WardrobaItem wardrobaItem=Constants.all_items.get(position);
   		 	View vi=null;
   			item =new GroupItem();

   	         
   			vi=mInflater.inflate(R.layout.home_activity_lay, parent,false);
   			item.linOwnerHeader = (LinearLayout)vi.findViewById(R.id.header);
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

   			 imgProductImage.callback=new onMyDoubleClickListener() {
   					
   					@Override
   					public void setLikeProduct() {
   						// TODO Auto-generated method stub
   						
   						
   						String LikeStatus;
   						//Toast.makeText(activity, "hello like count:"+like_count++,Toast.LENGTH_SHORT).show();
   						Constants.CLOTHISID = String.valueOf(wardrobaItem.getPIdCloth());
   						Constants.CLOTH_USERID = String.valueOf(wardrobaItem.getPUserId());
   						Constants.OBJECT_ID = String.valueOf(wardrobaItem.getPObjectId());
   						Constants.CLOTH_TYPE= wardrobaItem.getPClothType().toString().trim();
   						Log.d("Object id:", "object id:"+Constants.OBJECT_ID+" user id:"+Constants.CLOTH_USERID);
   						
   						LikeStatus = wardrobaItem.getPLikeStatus().toString().trim();
   						Log.d("BaseAdapter", "Like status:"+LikeStatus.toString());

   						Constants.SELECTED_ID=position;
   						int count_like = (wardrobaItem.getPLikeCount());
   						
   						String url="";
   						if(LikeStatus.equals("LIKE"))
   				        {
   							txtLikeCount.setText(String.valueOf(count_like=count_like+1));
   							btnLike.setBackgroundResource(R.drawable.like_h);
   							url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Constants.CLOTHISID+"&user_id="+Constants.CLOTH_USERID+"&object_id="+Constants.OBJECT_ID+"&cloth_type="+Constants.CLOTH_TYPE+"&status="+"LIKE";
   							try
   							{
   								WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.product_like,mContext);
   								Log.d("Like URL= ",url.toString());
   								webAPIHelper.execute(url);    
   								imgLikeDil.setVisibility(View.VISIBLE);
   								Animation anim=AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.fade_in);
   								//anim.setFillAfter(true);
   								//anim.setRepeatMode(Animation.REVERSE);
   								//callback.setLikeProduct();
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
   										Animation anim=AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.fade_out);
   										
   										//anim.setRepeatMode(Animation.REVERSE);
   										imgLikeDil.startAnimation(anim);
   									}
   								});
   							}
   							catch(Exception e)
   							{
   								
   							}
   					    }
   						else
   						{
   							Toast.makeText(mContext.getActivity(), "already like", Toast.LENGTH_SHORT).show();
   						}
   						
   					}
   				};

   			
   			btnLike.setOnClickListener(new View.OnClickListener() 
   			{
   				public void onClick(View v) 
   				{
   					
   							// TODO Auto-generated method stub
   								String LikeStatus;
   								String url;
   								Constants.CLOTHISID = String.valueOf(wardrobaItem.getPIdCloth());
   								Constants.CLOTH_USERID = String.valueOf(wardrobaItem.getPUserId());
   								Constants.OBJECT_ID = String.valueOf(wardrobaItem.getPObjectId());
   								Constants.CLOTH_TYPE= wardrobaItem.getPClothType().toString().trim();
   								Log.d("Object id:", "object id:"+Constants.OBJECT_ID+" user id:"+Constants.CLOTH_USERID);
   								
   								LikeStatus = wardrobaItem.getPLikeStatus().toString().trim();
   								Log.d("BaseAdapter", "Like status:"+LikeStatus.toString());

   								Constants.SELECTED_ID=position;
   								int count_like = (wardrobaItem.getPLikeCount());
   						
   								if(LikeStatus.equals("LIKE"))
   						        {
   									txtLikeCount.setText(String.valueOf(count_like=count_like+1));
   									btnLike.setBackgroundResource(R.drawable.like_h);
   									url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Constants.CLOTHISID+"&user_id="+Constants.CLOTH_USERID+"&object_id="+Constants.OBJECT_ID+"&cloth_type="+Constants.CLOTH_TYPE+"&status="+"LIKE";
   									Constants.all_items.get(Constants.SELECTED_ID).setPLikeStatus("UNLIKE");
   									Constants.all_items.get(Constants.SELECTED_ID).setPLikeCount(count_like);
   						        }else
   						        {
   						        	txtLikeCount.setText(String.valueOf(count_like=count_like-1));
   						        	btnLike.setBackgroundResource(R.drawable.like);
   						        	url = Constants.PRODUCT_LIKE_URL+"&id_cloth="+Constants.CLOTHISID+"&user_id="+Constants.CLOTH_USERID+"&object_id="+Constants.OBJECT_ID+"&cloth_type="+Constants.CLOTH_TYPE+"&status="+"UNLIKE";
   						        	Constants.all_items.get(Constants.SELECTED_ID).setPLikeStatus("LIKE");
   						        	Constants.all_items.get(Constants.SELECTED_ID).setPLikeCount(count_like);
   						        }
   								try
   								{
   									 
   									
   									WebAPIHelper1 webAPIHelper = new WebAPIHelper1(Constants.product_like,mContext);
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
   					Log.d("Object id:", "object id:"+Constants.OBJECT_ID+" user id:"+Constants.CLOTH_USERID.toString());
   					Log.d("BaseAdapter", "Object ID:"+String.valueOf(Constants.all_items.get(position).getPObjectId()));
   					Intent intent=new Intent(mContext.getActivity(),CommentViewActivity.class);
   					mContext.startActivity(intent);
   				}
   			});
   		
   			
   			btnShare.setOnClickListener(new View.OnClickListener() 
   			{
   				public void onClick(View v) 
   				{
   					//Toast.makeText(activity,"Share", 5000).show();
   					SharDialog.setVisibility(View.VISIBLE);
   					Animation anim=AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.slide_up_anim);
   					anim.setFillAfter(true);
   					SharDialog.startAnimation(anim);
   				}
   			});
   			imgProductImage.setDilImage(imgLikeDil);
   			//gestureListener=new GestureListener();
   			
   			
   			
               item.linOwnerHeader.setOnClickListener(new View.OnClickListener() 
           	{
       			public void onClick(View v)
       			{
       				Constants.OWNERID= wardrobaItem.getPOwnerId();
       				ProfileOwnerViewFragment secondFragment=new ProfileOwnerViewFragment();
    				
    	
    				FragmentTransaction transaction = getFragmentManager().beginTransaction();
    	            transaction.replace(R.id.home_fragment_container, secondFragment);
    	            transaction.addToBackStack(null);

    	            // Commit the transaction
    	            transaction.commit();
       				
       			}
       		});
   			

   		return vi;	
   	}	
   	
   	
//   	private static class ViewHolder 
//   	{
//   		TextView header;
//   		int previousTop = 0;
//   	}

   	
       
   	
   	


   	
        
   }
}
